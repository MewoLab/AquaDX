import { t, ts } from "../../libs/i18n";
import useLocalStorage from "../../libs/hooks/useLocalStorage.svelte";

const isDirectory = (e: FileSystemEntry): e is FileSystemDirectoryEntry => e.isDirectory
const isFile = (e: FileSystemEntry): e is FileSystemFileEntry => e.isFile

const getDirectory = (directory: FileSystemDirectoryEntry, path: string): Promise<FileSystemEntry> => new Promise((res, rej) => directory.getDirectory(path, {}, d => res(d), e => rej()));
const getFile = (directory: FileSystemDirectoryEntry, path: string): Promise<FileSystemEntry> => new Promise((res, rej) => directory.getFile(path, {}, d => res(d), e => rej()));
const getFiles = async (directory: FileSystemDirectoryEntry): Promise<Array<FileSystemEntry>> => {
    let reader = directory.createReader();
    let files: Array<FileSystemEntry> = [];
    let currentFiles: number = 1e9;
    while (currentFiles != 0) {
        let entries = await new Promise<Array<FileSystemEntry>>(r => reader.readEntries(r));
        files = files.concat(entries);
        currentFiles = entries.length;
    }
    return files;
};

const validateDirectories = async (base: FileSystemDirectoryEntry, path: string): Promise<boolean> => {
    const pathTrail = path.split("/");
    let directory: FileSystemDirectoryEntry = base;
    for (let part of pathTrail) {
        let newDirectory = await getDirectory(directory, part).catch(_ => null);
        if (newDirectory && isDirectory(newDirectory)) {
            directory = newDirectory;
        } else 
            return false;
    };
    return true
}

const getDirectoryFromPath = async (base: FileSystemDirectoryEntry, path: string): Promise<FileSystemDirectoryEntry | null> => {
    const pathTrail = path.split("/");
    let directory: FileSystemDirectoryEntry = base;
    for (let part of pathTrail) {
        let newDirectory = await getDirectory(directory, part).catch(_ => null);
        if (newDirectory && isDirectory(newDirectory)) {
            directory = newDirectory;
        } else 
            return null;
    };
    return directory;
}

export let ddsDB: IDBDatabase | undefined ;

/* Technically, processName should be in the translation file but I figured it was such a small thing that it didn't REALLY matter... */
const DIRECTORY_PATHS = ([
    {
        folder: "ddsImage",
        processName: "Characters",
        path: "characterThumbnail",
        filter: (name: string) => name.substring(name.length - 6, name.length) == "02.dds",
        id: (name: string) => `0${name.substring(17, 21)}${name.substring(23, 24)}`
    },
    {
        folder: "namePlate",
        processName: "Nameplates",
        path: "nameplate",
        filter: () => true,
        id: (name: string) => name.substring(17, 25)
    },
    {
        folder: "avatarAccessory",
        processName: "Avatar Accessory Thumbnails",
        path: "avatarAccessoryThumbnail",
        filter: (name: string) => name.substring(14, 18) == "Icon",
        id: (name: string) => name.substring(19, 27)
    },
    {
        folder: "avatarAccessory",
        processName: "Avatar Accessories",
        path: "avatarAccessory",
        filter: (name: string) => name.substring(14, 17) == "Tex",
        id: (name: string) => name.substring(18, 26)
    },
    {
        folder: "texture",
        processName: "Surfboard Textures",
        useFileName: true,
        path: "surfboard",
        filter: (name: string) => 
            ([
                "CHU_UI_Common_Avatar_body_00.dds",
                "CHU_UI_Common_Avatar_face_00.dds",
                "CHU_UI_title_rank_00_v10.dds"
            ]).includes(name),
        id: (name: string) => name
    }
] satisfies {folder: string, processName: string, path: string, useFileName?: boolean, filter: (name: string) => boolean, id: (name: string) => string}[] )

export const scanOptionFolder = async (optionFolder: FileSystemDirectoryEntry, progressUpdate: (progress: number, text: string) => void) => {
    let filesToProcess: Record<string, FileSystemFileEntry[]> = {};
    let directories = (await getFiles(optionFolder))
        .filter(directory => isDirectory(directory) && ((directory.name.substring(0, 1) == "A" && directory.name.length == 4) || directory.name == "surfboard"))

    for (let directory of directories)
        if (isDirectory(directory)) {
            for (const directoryData of DIRECTORY_PATHS) {
                let folder = await getDirectoryFromPath(directory, directoryData.folder).catch(_ => null) ?? [];
                if (folder) {
                    if (!filesToProcess[directoryData.path])
                        filesToProcess[directoryData.path] = [];
                    for (let dataFolderEntry of await getFiles(folder as FileSystemDirectoryEntry).catch(_ => null) ?? [])
                        if (isDirectory(dataFolderEntry)) {
                            for (let dataEntry of await getFiles(dataFolderEntry as FileSystemDirectoryEntry).catch(_ => null) ?? [])
                                if (isFile(dataEntry) && directoryData.filter(dataEntry.name))
                                    filesToProcess[directoryData.path].push(dataEntry);
                        } else if (isFile(dataFolderEntry) && directoryData.filter(dataFolderEntry.name))
                            filesToProcess[directoryData.path].push(dataFolderEntry);
                }
            }
        }

    let data = [];

    for (const [folder, files] of Object.entries(filesToProcess)) {
        let reference = DIRECTORY_PATHS.find(r => r.path == folder);
        for (const [idx, file] of files.entries()) {
            progressUpdate((idx / files.length) * 100, `${t("userbox.new.setup.processing_file")} ${reference?.processName ?? "?"}...`)
            data.push({
                path: `${folder}:${reference?.id(file.name)}`, name: file.name, blob: await new Promise<File>(res => file.file(res))
            });
        }
    }

    progressUpdate(100, `${t("userbox.new.setup.finalizing")}...`)

    let transaction = ddsDB?.transaction(['dds'], 'readwrite', { durability: "strict" })
    if (!transaction) return; // TODO: bubble error up to user
    transaction.onerror = e => e.preventDefault()
    let objectStore = transaction.objectStore('dds');
    for (let object of data)
        objectStore.put(object)
    
    // await transaction completion
    await new Promise(r => transaction.addEventListener("complete", r, {once: true}))
};

export function initializeDb() : Promise<void> {
    return new Promise(r => {
        const dbRequest = indexedDB.open("userboxChusanDDS", 1)
        dbRequest.addEventListener("upgradeneeded", (event) => {
            if (!(event.target instanceof IDBOpenDBRequest)) return
            ddsDB = event.target.result;
            if (!ddsDB) return;

            const store = ddsDB.createObjectStore('dds', { keyPath: 'path' });
            store.createIndex('path', 'path', { unique: true })
            store.createIndex('name', 'name', { unique: false })
            store.createIndex('blob', 'blob', { unique: false })
            r();
        });
        dbRequest.addEventListener("success", () => {
            ddsDB = dbRequest.result;
            r();
        })
    })
}

export async function userboxFileProcess(folder: FileSystemEntry, progressUpdate: (progress: number, progressString: string) => void): Promise<string | null> {
    if (!isDirectory(folder))
        return t("userbox.new.error.invalidFolder")
    if (!(await validateDirectories(folder, "bin/option")) || !(await validateDirectories(folder, "data/A000")))
        return t("userbox.new.error.invalidFolder");

    initializeDb();
    const optionFolder = await getDirectoryFromPath(folder, "bin/option");
    if (optionFolder)
        await scanOptionFolder(optionFolder, progressUpdate);
    const dataFolder = await getDirectoryFromPath(folder, "data");
    if (dataFolder)
        await scanOptionFolder(dataFolder, progressUpdate);
    useLocalStorage("userboxNew", false).value = true;
    useLocalStorage("userboxNewProfile", false).value = true;
    location.reload();

    return null
}