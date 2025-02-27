/* 

A simplified DDS parser with Chusan userbox in mind.
There are some issues on Safari. I don't really care, to be honest.
Authored by Raymond and May.

DDS header parsing based off of https://gist.github.com/brett19/13c83c2e5e38933757c2

*/

import DDSCache from "./ddsCache";

function makeFourCC(string: string) {
    return string.charCodeAt(0) +
          (string.charCodeAt(1) << 8) +
          (string.charCodeAt(2) << 16) +
          (string.charCodeAt(3) << 24);
};

/**
 * @description Magic bytes for the DDS file format (see https://en.wikipedia.org/wiki/Magic_number_(programming))
 */
const DDS_MAGIC_BYTES = 0x20534444;

/* 
    to get around the fact that TS's builtin Object.fromEntries() typing
    doesn't persist strict types and instead only uses broad types
    without creating a new function to get around it...
    sorry, this is a really ugly solution, but it's not my problem 
*/

/**
 * @description List of compression type markers used in DDS
 */
const DDS_COMPRESSION_TYPE_MARKERS = ["DXT1", "DXT3", "DXT5"] as const;

/**
 * @description Object mapping string versions of DDS compression type markers to their value in uint32s
 */
const DDS_COMPRESSION_TYPE_MARKERS_MAP = Object.fromEntries(
    DDS_COMPRESSION_TYPE_MARKERS
        .map(e => [e, makeFourCC(e)] as [typeof e, number])
) as Record<typeof DDS_COMPRESSION_TYPE_MARKERS[number], number>

const DDS_DECOMPRESS_VERTEX_SHADER = `
attribute vec2 aPosition;
varying highp vec2 vTextureCoord;
void main() {
    gl_Position = vec4(aPosition, 0.0, 1.0);
    vTextureCoord = ((aPosition * vec2(1.0, -1.0)) / 2.0 + 0.5);
}`;
const DDS_DECOMPRESS_FRAGMENT_SHADER = `
varying highp vec2 vTextureCoord;
uniform sampler2D uTexture;
void main() {
    gl_FragColor =  texture2D(uTexture, vTextureCoord);
}`

export interface RGB {
    r: number,
    g: number,
    b: number
}

export class DDS {
    constructor(db: IDBDatabase | undefined) {
        this.cache = new DDSCache(db);

        let gl = this.canvasGL.getContext("webgl");
        if (!gl) throw new Error("Failed to get WebGL rendering context") // TODO: make it switch to Classic userbox
        this.gl = gl;

        let ctx = this.canvas2D.getContext("2d");
        if (!ctx) throw new Error("Failed to reach minimum system requirements") // TODO: make it switch to Classic userbox
        this.ctx = ctx;

        let ext =
            gl.getExtension("WEBGL_compressed_texture_s3tc") ||
            gl.getExtension("MOZ_WEBGL_compressed_texture_s3tc") ||
            gl.getExtension("WEBKIT_WEBGL_compressed_texture_s3tc");
        if (!ext) throw new Error("Browser is not supported."); // TODO: make it switch to Classic userbox
        this.ext = ext;

        /* Initialize shaders */
        this.compileShaders();
        this.gl.useProgram(this.shader);
        
        /* Setup position buffer */
        let attributeLocation = this.gl.getAttribLocation(this.shader ?? 0, "aPosition");
        let positionBuffer = this.gl.createBuffer();
        this.gl.bindBuffer(this.gl.ARRAY_BUFFER, positionBuffer);
        this.gl.bufferData(this.gl.ARRAY_BUFFER, new Float32Array([1.0, 1.0, -1.0, 1.0, 1.0, -1.0, -1.0, -1.0]), this.gl.STATIC_DRAW);

        this.gl.vertexAttribPointer(
            attributeLocation,
            2, this.gl.FLOAT,
            false, 0, 0
        );
        this.gl.enableVertexAttribArray(attributeLocation)
    }
    
    /**
     * @description Loads a DDS file into the internal canvas object.
     * @param buffer Uint8Array to load DDS from. 
     * @returns String if failed to load, void if success
     */
    load(buffer: Uint8Array) {
        let header = this.loadHeader(buffer);
        if (!header) return;
        
        let compressionMode: GLenum = this.ext.COMPRESSED_RGBA_S3TC_DXT1_EXT;

        if (header.pixelFormat.flags & 0x4) {
            switch (header.pixelFormat.type) {
                case DDS_COMPRESSION_TYPE_MARKERS_MAP.DXT1: 
                    compressionMode = this.ext.COMPRESSED_RGBA_S3TC_DXT1_EXT;
                    break;
                case DDS_COMPRESSION_TYPE_MARKERS_MAP.DXT3:
                    compressionMode = this.ext.COMPRESSED_RGBA_S3TC_DXT3_EXT;
                    break;
                case DDS_COMPRESSION_TYPE_MARKERS_MAP.DXT5:
                    compressionMode = this.ext.COMPRESSED_RGBA_S3TC_DXT5_EXT;
                    break;
            };
        } else return; 

        /* Initialize and configure the texture */
        let texture = this.gl.createTexture();
        this.gl.activeTexture(this.gl.TEXTURE0);
        this.gl.bindTexture(this.gl.TEXTURE_2D, texture);

        this.gl.texParameteri(this.gl.TEXTURE_2D, this.gl.TEXTURE_MIN_FILTER, this.gl.LINEAR);
        this.gl.texParameteri(this.gl.TEXTURE_2D, this.gl.TEXTURE_MAG_FILTER, this.gl.LINEAR);
        this.gl.texParameteri(this.gl.TEXTURE_2D, this.gl.TEXTURE_WRAP_S, this.gl.CLAMP_TO_EDGE);
        this.gl.texParameteri(this.gl.TEXTURE_2D, this.gl.TEXTURE_WRAP_T, this.gl.CLAMP_TO_EDGE);

        this.gl.compressedTexImage2D(
            this.gl.TEXTURE_2D,
            0,
            compressionMode,
            header.width,
            header.height,
            0,
            buffer.slice(128)
        );
        
        this.gl.uniform1i(this.gl.getUniformLocation(this.shader || 0, "uTexture"), 0);
    
        /* Prepare the canvas for drawing */
        this.canvasGL.width = header.width;
        this.canvasGL.height = header.height
        this.gl.viewport(0, 0, this.canvasGL.width, this.canvasGL.height);

        this.gl.clearColor(0.0, 0.0, 0.0, 0.0);
        this.gl.clear(this.gl.COLOR_BUFFER_BIT);

        this.gl.drawArrays(this.gl.TRIANGLE_STRIP, 0, 4);
        this.gl.deleteTexture(texture);
    };
    
    /**
     * @description Export a Blob from the parsed DDS texture
     * @returns DDS texture in specified format
     * @param inFormat Mime type to export in
     */
    getBlob(inFormat?: string): Promise<Blob | null> {
        return new Promise(res => this.canvasGL.toBlob(res, inFormat))
    }
    get2DBlob(inFormat?: string): Promise<Blob | null> {
        return new Promise(res => this.canvas2D.toBlob(res, inFormat))
    }
    
    /**
     * @description Helper function to load in a Blob
     * @input Blob to use
     */
    async fromBlob(input: Blob) {
        this.load(new Uint8Array(await input.arrayBuffer()));
    }
    
    /**
     * @description Read a DDS file header
     * @param buffer Uint8Array of the DDS file's contents
     */
    loadHeader(buffer: Uint8Array) {
        if (this.getUint32(buffer, 0) !== DDS_MAGIC_BYTES) return;
        
        return {
            size: this.getUint32(buffer, 4),
            flags: this.getUint32(buffer, 8),
            height: this.getUint32(buffer, 12),
            width: this.getUint32(buffer, 16),
            mipmaps: this.getUint32(buffer, 24),
            
            /* TODO: figure out if we can cut any of this out (we totally can btw) */
            pixelFormat: {
                size: this.getUint32(buffer, 76),
                flags: this.getUint32(buffer, 80),
                type: this.getUint32(buffer, 84),
            }
        }
    };

    /**
     * @description Retrieve a file from the IndexedDB database and load it into the DDS loader
     * @param path File path
     * @returns Whether or not the attempt to retrieve the file was successful
     */
    loadFile(path: string) : Promise<boolean> {
        return new Promise(async r => {
            let file = await this.cache?.getFromDatabase(path)
            if (file != null)
                await this.fromBlob(file)
            r(file != null)
        })
    };
    
    /**
     * @description Retrieve a file from a path
     * @param path File path
     * @param fallback Path to a file to fallback to if loading this file fails
     * @returns An object URL which correlates to a Blob
     */
    async getFile(path: string, fallback?: string) : Promise<string> {
        if (this.cache?.cached(path))
            return this.cache.find(path) ?? ""
        if (!await this.loadFile(path))
            if (fallback) {
                if (!await this.loadFile(fallback))
                    return "";
            } else
                return ""
        let blob = await this.getBlob("image/png");
        if (!blob) return ""
        return this.cache?.save(
            path, URL.createObjectURL(blob)
        ) ?? "";
    };

    /**
     * @description Transform a spritesheet located at a path to match the dimensions specified in the parameters
     * @param path Spritesheet path
     * @param x Crop: X
     * @param y Crop: Y
     * @param w Crop: Width
     * @param h Crop: Height
     * @param s Scale factor
     * @returns An object URL which correlates to a Blob
     */
    async getFileFromSheet(path: string, x: number, y: number, w: number, h: number, s?: number, color?: RGB): Promise<string> {
        if (!await this.loadFile(path))
            return "";
        this.canvas2D.width = w * (s ?? 1);
        this.canvas2D.height = h * (s ?? 1);
        this.ctx.drawImage(this.canvasGL, x, y, w, h, 0, 0, w * (s ?? 1), h * (s ?? 1));
        
        if (color) {
            let colorData = this.ctx.getImageData(0, 0, this.canvas2D.width, this.canvas2D.height);
            for (let i = 0; colorData.data.length > i; i++)
                switch (i % 4) {
                    case 0:
                        colorData.data[i] *= (color.r / 255); break;
                    case 1:
                        colorData.data[i] *= (color.g / 255); break;
                    case 2:
                        colorData.data[i] *= (color.b / 255); break;
                }
            this.ctx.putImageData(colorData, 0, 0);
        }

        /* We don't want to cache this, it's a spritesheet piece. */
        return URL.createObjectURL(await this.get2DBlob("image/png") ?? new Blob([]));
    };
    
    /**
     * @description Retrieve a file and scale it by a specified scale factor 
     * @param path File path
     * @param s Scale factor
     * @param fallback Path to a file to fallback to if loading this file fails
     * @returns An object URL which correlates to a Blob
     */
    async getFileScaled(path: string, s: number, fallback?: string): Promise<string> {
        if (this.cache?.cached(path, s))
            return this.cache.find(path, s) ?? ""
        if (!await this.loadFile(path))
            if (fallback) {
                if (!await this.loadFile(fallback))
                    return "";
            } else
                return "";
        this.canvas2D.width = this.canvasGL.width * (s ?? 1);
        this.canvas2D.height = this.canvasGL.height * (s ?? 1);
        this.ctx.drawImage(this.canvasGL, 0, 0, this.canvasGL.width, this.canvasGL.height, 0, 0, this.canvasGL.width * (s ?? 1), this.canvasGL.height * (s ?? 1));

        return this.cache?.save(path, URL.createObjectURL(await this.get2DBlob("image/png") ?? new Blob([])), s) ?? "";
    };

    /**
     * @description Retrieve a Uint32 from a Uint8Array at the specified offset
     * @param buffer Uint8Array to retrieve the Uint32 from
     * @param offset Offset at which to retrieve bytes
     */
    getUint32(buffer: Uint8Array, offset: number) {
        return (buffer[offset + 0] << 0) +
            (buffer[offset + 1] << 8) +
            (buffer[offset + 2] << 16) +
            (buffer[offset + 3] << 24);
    };
    
    private compileShaders() {
        let vertexShader = this.gl.createShader(this.gl.VERTEX_SHADER);
        let fragmentShader = this.gl.createShader(this.gl.FRAGMENT_SHADER);

        if (!vertexShader || !fragmentShader) return;

        this.gl.shaderSource(vertexShader, DDS_DECOMPRESS_VERTEX_SHADER);
        this.gl.compileShader(vertexShader);

        if (!this.gl.getShaderParameter(vertexShader, this.gl.COMPILE_STATUS))
            throw new Error(
                `An error occurred compiling vertex shader: ${this.gl.getShaderInfoLog(vertexShader)}`,
            );

        this.gl.shaderSource(fragmentShader, DDS_DECOMPRESS_FRAGMENT_SHADER);
        this.gl.compileShader(fragmentShader);

        if (!this.gl.getShaderParameter(fragmentShader, this.gl.COMPILE_STATUS))
            throw new Error(
                `An error occurred compiling fragment shader: ${this.gl.getShaderInfoLog(fragmentShader)}`,
            );

        let program = this.gl.createProgram();

        if (!program) return;
        this.shader = program;

        this.gl.attachShader(program, vertexShader);
        this.gl.attachShader(program, fragmentShader);
        this.gl.linkProgram(program);

        if (!this.gl.getProgramParameter(program, this.gl.LINK_STATUS))
            throw new Error(
                `An error occurred linking the program: ${this.gl.getProgramInfoLog(program)}`,
            );
    };

    canvas2D: HTMLCanvasElement = document.createElement("canvas");
    canvasGL: HTMLCanvasElement = document.createElement("canvas");

    cache: DDSCache | null;
    ctx: CanvasRenderingContext2D;

    gl: WebGLRenderingContext;
    ext: ReturnType<typeof this.gl.getExtension>;
    shader: WebGLShader | null = null;
};