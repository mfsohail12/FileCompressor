import React from "react";
import { useState } from "react";
import axios from "axios";

const Compress = () => {
  const [file, setFile] = React.useState(null);
  const [compressedFile, setCompressedFile] = React.useState(null);
  const [loading, setLoading] = React.useState(false);

  const handleFileChange = (e) => {
    if (e.target.files) {
      setFile(e.target.files[0]); // get first file input
    }
  };

  const compressFile = async () => {
    setLoading(true);
    const formData = new FormData();
    formData.append("file", file); // Attach file to request

    try {
      const response = await axios.post(
        "http://localhost:8080/compression/encode",
        formData,
        {
          responseType: "blob",
        }
      );

      // Create a download link for the received .bin file
      const url = window.URL.createObjectURL(new Blob([response.data]));
      const a = document.createElement("a");
      a.href = url;
      a.download = "compressed.bin"; // Set the filename
      document.body.appendChild(a);
      a.click();
      a.remove();
    } catch (error) {
      console.error("Error:", error);
      alert("Failed to compress file.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <>
      <p className="mb-4 font-bold">Compress your file:</p>
      <input
        type="file"
        accept=".txt"
        className="file-input file-input-bordered file-input-error w-full max-w-xs"
        onChange={handleFileChange}
      />
      {file && (
        <div className="mt-4 text-sm w-80 ">
          <p>File name: {file.name}</p>
          <p>Size: {(file.size / 1024).toFixed(2)} KB</p>
        </div>
      )}

      {file && (
        <button
          className="btn btn-outline btn-error mt-6 w-40"
          onClick={compressFile}
          disabled={loading}
        >
          {loading ? "Compressing..." : "Compress"}
        </button>
      )}
    </>
  );
};

export default Compress;
