import { RiFileDownloadLine } from "react-icons/ri";

const FileDownload = ({ file, type }) => {
  const downloadFile = () => {
    const url = window.URL.createObjectURL(new Blob([file.data]));
    const a = document.createElement("a");
    a.href = url;
    a.download = file.name.split(".")[0].concat(type);
    document.body.appendChild(a);
    a.click();
    a.remove();
  };

  return (
    <button
      className="w-80 rounded-md p-3 border-1 flex gap-3 hover:border-red-400 hover:text-red-400"
      onClick={downloadFile}
    >
      <RiFileDownloadLine className="text-5xl" />
      <span className="flex flex-col items-start">
        <p>File: {file.name.split(".")[0].concat(type)}</p>
        <p>Size: {(file.data.size / 1024).toFixed(2)} KB</p>
      </span>
    </button>
  );
};

export default FileDownload;
