import { useState } from "react";
import axios from "axios";
import FileDownload from "./FileDownload";
import TextPreview from "./TextPreview";

const Compress = () => {
  const [file, setFile] = useState(null);
  const [decompressedFile, setdecompressedFile] = useState("");
  const [decompressedText, setDecompressedText] = useState("");

  const handleFileChange = (e) => {
    if (e.target.files) {
      setFile(e.target.files[0]); // Get first file input
    }
  };

  const handleDecompress = async () => {
    if (!file) return;

    const formData = new FormData();
    formData.append("file", file);

    try {
      const response = await axios.post("/compression/decode", formData, {
        headers: {
          "Content-Type": "multipart/form-data",
        },
      });

      if (!response) throw new Error("No decompressed file data found");

      const blob = new Blob([response.data], { type: "text/plain" });

      setdecompressedFile({ data: blob, name: file.name });
      setDecompressedText(response.data);
    } catch (error) {
      console.error("Error decompressing file:", error);
    }
  };

  const handleDownload = () => {
    const blob = new Blob([decompressedFile], { type: "text/plain" });
    const link = document.createElement("a");
    link.href = URL.createObjectURL(blob);
    link.download = "decompressed.txt";
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
  };

  return (
    <div className="flex flex-col items-center">
      {!decompressedFile && (
        <>
          <p className="mb-4 font-bold text-white">Decompress your file:</p>
          <input
            type="file"
            accept=".bin"
            className="file-input file-input-bordered file-input-error w-80 max-w-xs bg-[#1D232A] text-white"
            onChange={handleFileChange}
          />
          {file && (
            <div className="mt-4 text-sm w-80 text-white">
              <p>File name: {file.name}</p>
              <p>Size: {(file.size / 1024).toFixed(2)} KB</p>
            </div>
          )}
        </>
      )}

      {!decompressedFile && file && (
        <button
          className="btn btn-outline btn-error mt-6 w-40"
          onClick={handleDecompress}
        >
          Decompress
        </button>
      )}
      {!file && (
        <p className="text-sm mt-8 text-white">
          Please provide the compressed .bin file
        </p>
      )}
      {decompressedFile && (
        <>
          <FileDownload file={decompressedFile} type={".txt"} />
          <TextPreview text={decompressedText} />
        </>
      )}
    </div>
  );
};

export default Compress;
