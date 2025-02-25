import React, { useState } from "react";
import axios from "axios";

const Compress = () => {
  const [file, setFile] = useState(null);
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
      const response = await axios.post(
        "http://localhost:8080/compression/decode",
        formData,
        {
          headers: {
            "Content-Type": "multipart/form-data",
          },
        }
      );

      setDecompressedText(response.data);
    } catch (error) {
      console.error("Error decompressing file:", error);
    }
  };

  const handleDownload = () => {
    const blob = new Blob([decompressedText], { type: "text/plain" });
    const link = document.createElement("a");
    link.href = URL.createObjectURL(blob);
    link.download = "decompressed.txt";
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
  };

  return (
    <div className="flex flex-col items-center">
      <p className="mb-4 font-bold">Decompress your file:</p>
      <input
        type="file"
        accept=".bin"
        className="file-input file-input-bordered file-input-error w-80 max-w-xs"
        onChange={handleFileChange}
      />
      {file && (
        <div className="mt-4 text-sm w-80">
          <p>File name: {file.name}</p>
          <p>Size: {(file.size / 1024).toFixed(2)} KB</p>
        </div>
      )}

      {file && (
        <button
          className="btn btn-outline btn-error mt-6 w-40"
          onClick={handleDecompress}
        >
          Decompress
        </button>
      )}

      {decompressedText && (
        <div className="mt-6 w-80">
          <p className="font-semibold">Decompressed Text Preview:</p>
          <textarea
            className="textarea textarea-bordered w-full mt-2"
            value={decompressedText}
            readOnly
            rows={5}
          ></textarea>
          <button
            className="btn btn-success mt-4 w-full"
            onClick={handleDownload}
          >
            Download Decompressed File
          </button>
        </div>
      )}
    </div>
  );
};

export default Compress;
