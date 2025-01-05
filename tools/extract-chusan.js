/* 

Chusan asset extractor for AquaBox URL mode.
    Place your "option" (or "bin/option") and "data" folders in the same directory as this script as they're named.

    Data will be placed into the "chu3" folder.
    Place the contents into a public directory that can be accessed by users.

Know Python or another common scripting language?
    Feel free to rewrite this tool and submit it to MewoLab/AquaDX!
    Or rewrite it in JavaScript again! Anything is better than this hot pile of garbage!

*/

// Allows this to be a single-file script
const fs = require("fs");

const verifyDirectoryExistant = (name) => {
    return fs.existsSync(name);
}
const mkdir = (name) => {
    if (!fs.existsSync(name))
        fs.mkdirSync(name);
};
const outputTarget = "chu3";

const directoryPaths = [
    {
        folder: "ddsImage",
        processName: "Characters",
        path: "characterThumbnail",
        filter: (name) => name.substring(name.length - 6, name.length) == "02.dds",
        id: (name) => `0${name.substring(17, 21)}${name.substring(23, 24)}`
    },
    {
        folder: "namePlate",
        processName: "Nameplates",
        path: "nameplate",
        filter: (name) => name.substring(0, 17) == "CHU_UI_NamePlate_",
        id: (name) => name.substring(17, 25)
    },
    {
        folder: "avatarAccessory",
        processName: "Avatar Accessory Thumbnails",
        path: "avatarAccessoryThumbnail",
        filter: (name) => name.substring(14, 18) == "Icon",
        id: (name) => name.substring(19, 27)
    },
    {
        folder: "avatarAccessory",
        processName: "Avatar Accessories",
        path: "avatarAccessory",
        filter: (name) => name.substring(14, 17) == "Tex",
        id: (name) => name.substring(18, 26)
    },
    {
        folder: "texture",
        processName: "Surfboard Textures",
        useFileName: true,
        path: "surfboard",
        filter: (name) => 
            ([
                "CHU_UI_Common_Avatar_body_00.dds",
                "CHU_UI_Common_Avatar_face_00.dds",
                "CHU_UI_title_rank_00_v10.dds"
            ]).includes(name),
        id: (name) => name
    }
];

const processFile = (fileName, path, subFolder) => {
    let localReference = directoryPaths.find(p => p.folder == subFolder && p.filter(fileName));
    if (!localReference) return;
    files.push({
        path: `${path}/${fileName}`,
        target: `${localReference.id(fileName)}.chu`,
        targetFolder: `${localReference.path}`,
        name: fileName
    });
}

let files = [];
const processFolder = (path) => {
    for (const folder of fs.readdirSync(path)) {
        let folderData = fs.statSync(`${path}/${folder}`);
        if (!folderData.isDirectory()) continue;
        for (const subFolder of fs.readdirSync(`${path}/${folder}`)) {
            let folderData = fs.statSync(`${path}/${folder}/${subFolder}`);
            let reference = directoryPaths.find(p => p.folder == subFolder);
            if (!reference || !folderData.isDirectory()) continue;
            // what a mess
            for (const subSubFolder of fs.readdirSync(`${path}/${folder}/${subFolder}`))
                if (fs.statSync(`${path}/${folder}/${subFolder}/${subSubFolder}`).isDirectory()) {
                    for (const subSubSubFile of fs.readdirSync(`${path}/${folder}/${subFolder}/${subSubFolder}`))
                        processFile(subSubSubFile, `${path}/${folder}/${subFolder}/${subSubFolder}`, subFolder)
                } else
                    processFile(subSubFolder, `${path}/${folder}/${subFolder}`, subFolder)
        }
    }
}

if (!verifyDirectoryExistant("data"))
    return console.log("Data folder non-existant.")
if (!verifyDirectoryExistant("bin"))
    if (!verifyDirectoryExistant("option"))
        return console.log("Option folder non-existant.")

processFolder("data");
if (verifyDirectoryExistant("bin")) {
    processFolder("bin/option");
} else
    processFolder("option");


console.log(`Found ${files.length} files.`);
console.log(`Copying now, please wait.`)

if (verifyDirectoryExistant(outputTarget))
    return console.log("Output folder exists.");
mkdir(outputTarget);

files.forEach(fileData => {
    console.log(`Copying ${fileData.name}`)
    mkdir(`${outputTarget}/${fileData.targetFolder}`)
    fs.copyFileSync(fileData.path, `${outputTarget}/${fileData.targetFolder}/${fileData.target}`)
})