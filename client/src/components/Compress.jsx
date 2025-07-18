import { useState, useEffect } from "react";
import axios from "axios";
import FileDownload from "./FileDownload";
import TextPreview from "./TextPreview";

const Compress = () => {
  const [file, setFile] = useState(null);
  const [fileText, setFileText] = useState("");
  const [compressedFile, setCompressedFile] = useState(null);
  const [loading, setLoading] = useState(false);

  console.log(fileText);

  const handleFileChange = (e) => {
    if (e.target.files) {
      setFile(e.target.files[0]); // get first file input
    }
  };

  const readFile = () => {
    if (file) {
      const reader = new FileReader();

      reader.onload = () => {
        setFileText(reader.result);
      };

      reader.onerror = () => {
        showMessage("Error reading the file. Please try again.", "error");
      };

      reader.readAsText(file);
    }
  };

  useEffect(() => {
    readFile();
  }, [file]);

  const compressFile = async () => {
    setLoading(true);
    const formData = new FormData();
    formData.append("file", file); // Attach file to request

    try {
      const response = await axios.post("/compression/encode", formData, {
        responseType: "blob",
      });

      if (!response) throw new Error("No compressed file data found");

      setCompressedFile({ data: response.data, name: file.name });
    } catch (error) {
      console.error("Error:", error);
      alert("Failed to compress file.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <>
      {!compressedFile && (
        <>
          <p className="mb-4 font-bold text-white">Compress your file:</p>
          <input
            type="file"
            accept=".txt"
            className="file-input file-input-bordered file-input-error w-80 max-w-xs bg-[#1D232A] text-white"
            onChange={handleFileChange}
          />
          {file && (
            <>
              <div className="mt-4 text-sm w-80 text-white">
                <p>File name: {file.name}</p>
                <p>Size: {(file.size / 1024).toFixed(2)} KB</p>
              </div>
              <TextPreview text={fileText} />
            </>
          )}
        </>
      )}

      {!compressedFile && file && (
        <button
          className="btn btn-outline btn-error mt-6 w-40"
          onClick={compressFile}
          disabled={loading}
        >
          {loading ? "Compressing..." : "Compress"}
        </button>
      )}
      {!file && (
        <p className="text-sm mt-8 text-white">Please provide a .txt file</p>
      )}
      {compressedFile && <FileDownload file={compressedFile} type={".bin"} />}
    </>
  );
};

export default Compress;
