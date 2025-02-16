import React from "react";
import { useState } from "react";

const Compress = () => {
  const [file, setFile] = React.useState(null);

  const handleFileChange = (e) => {
    if (e.target.files) {
      setFile(e.target.files[0]); // get first file input
    }
  };

  return (
    <>
      <p className="mb-4 font-bold">Decompress your file:</p>
      <input
        type="file"
        accept=".bin"
        className="file-input file-input-bordered file-input-error w-full max-w-xs"
        onChange={handleFileChange}
      />
      {file && (
        <div className="mt-4 text-sm w-80 ">
          <p>File name: {file.name}</p>
          <p>Size: {(file.size / 1024).toFixed(2)} KB</p>
        </div>
      )}
    </>
  );
};

export default Compress;
