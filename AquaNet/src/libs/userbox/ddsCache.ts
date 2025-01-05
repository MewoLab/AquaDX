import useLocalStorage from "../hooks/useLocalStorage.svelte";
import { USERBOX_DEFAULT_URL } from "../config";

export default class DDSCache {
    constructor(db: IDBDatabase | undefined) {
        this.db = db;
    }

    /**
     * @description Finds an object URL for the image with the specified path and scale
     * @param path Image path
     * @param scale Scale factor
     */
    find(path: string, scale: number = 1): string | undefined {
        return (this.urlCache.find(
            p => p.path == path && p.scale == scale)?.url)
    }

    /**
     * @description Checks whether an object URL is cached for the image with the specified path and scale
     * @param path Image path
     * @param scale Scale factor
     */
    cached(path: string, scale: number = 1): boolean {
        return this.urlCache.some(
            p => p.path == path && p.scale == scale)
    }

    /**
     * @description Save an object URL for the specified path and scale to the cache
     * @param path Image path
     * @param url Object URL
     * @param scale Scale factor
     */
    save(path: string, url: string, scale: number = 1) {
        if (this.cached(path, scale)) {
            URL.revokeObjectURL(url);
            return this.find(path, scale)
        }
        this.urlCache.push({path, url, scale})
        return url
    }

    /**
     * @description Retrieve a Blob from a database based on the specified path
     * @param path Image path
     */
    getFromDatabase(path: string): Promise<Blob | null> {
        return new Promise(async (resolve, reject) => {
            if (this.userboxURL.value) {
                let targetPath = path.replaceAll(":", "/");
                let response = await fetch(`${this.userboxURL.value}/${targetPath}.chu`).then(b => b.blob()).catch(reject);
                if (response)
                    return resolve(response);
            };
            if (!this.db)
                return resolve(null);
            let transaction = this.db.transaction(["dds"], "readonly");
            let objectStore = transaction.objectStore("dds");
            let request = objectStore.get(path);
            request.onsuccess = async (e) => {
                if (request.result)
                    if (request.result.blob)
                        return resolve(request.result.blob);
                return resolve(null);
            }
            request.onerror = () => resolve(null);
        })
    };

    private urlCache: {scale: number, path: string, url: string}[] = [];
    private db: IDBDatabase | undefined;
    userboxURL = useLocalStorage("userboxURL", USERBOX_DEFAULT_URL);
}