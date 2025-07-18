const TextPreview = ({ text }) => {
  return (
    <div className="mt-6 w-80">
      <p className="font-semibold">Text Preview:</p>
      <textarea
        className="textarea textarea-bordered w-full mt-2"
        value={text}
        readOnly
        rows={7}
      ></textarea>
    </div>
  );
};

export default TextPreview;
