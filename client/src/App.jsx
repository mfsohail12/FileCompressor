import { useState } from "react";
import axios from "axios";
import Navbar from "./components/Navbar";
import Hero from "./components/Hero";
import "./App.css";
import { FaGithub } from "react-icons/fa";

axios.defaults.baseURL = import.meta.env.VITE_SERVER_URL;

function App() {
  return (
    <>
      <Navbar />
      <Hero />
      <footer className="h-10 flex justify-center items-center absolute bottom-0 w-screen px-4">
        <a href="https://github.com/mfsohail12/FileCompressor">
          <FaGithub className="text-red-400 text-xl" />
        </a>
        <p className="text-white text-xs ml-auto">© 2025</p>
      </footer>
    </>
  );
}

export default App;
