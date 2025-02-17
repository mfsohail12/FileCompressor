import React from "react";
import Compress from "./Compress";
import Decompress from "./Decompress";

const Hero = () => {
  const [isCompress, setIsCompress] = React.useState(true);
  const compressBtnClass = isCompress ? "btn btn-error" : "btn";
  const decompressBtnClass = isCompress ? "btn" : "btn btn-error";

  return (
    <div className="min-h-screen flex items-center justify-center flex-col">
      <span className="mb-10 flex gap-5">
        <button
          className={compressBtnClass}
          onClick={() => setIsCompress(true)}
        >
          Compress a File
        </button>
        <button
          className={decompressBtnClass}
          onClick={() => setIsCompress(false)}
        >
          Decompress a File
        </button>
      </span>
      {isCompress ? <Compress /> : <Decompress />}
    </div>
  );
};

export default Hero;
