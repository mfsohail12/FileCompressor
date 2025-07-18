import { useState } from "react";
import axios from "axios";
import Navbar from "./components/Navbar";
import Hero from "./components/Hero";
import "./App.css";

axios.defaults.baseURL = import.meta.env.VITE_SERVER_URL;

function App() {
  return (
    <>
      <Navbar />
      <Hero />
    </>
  );
}

export default App;
