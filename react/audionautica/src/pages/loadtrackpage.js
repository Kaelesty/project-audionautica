import React, { useState } from "react";
import "./createplaylistpage.css"
import { useNavigate } from "react-router-dom"; 
import loadTrack from "../requests/loadtrack";
import LoaderIcon from "../components/search/download";

const LoadTrackForm = () => {
    const [formData, setFormData] = useState({
        file: "",
        title: "",
        artist: "",
        tags: "",
    });
    const navigate = useNavigate(); 
    const [loading, setLoading] = useState(false)

    const handleChange = (event) => {
        const { name, value, type, checked } = event.target;
        setFormData(prevState => ({
            ...prevState,
            [name]: type === "checkbox" ? checked : value
        }));
    };

    const handleFileChange = (event) => {
        const file = event.target.files[0];
        const reader = new FileReader();
        reader.onloadend = () => {
            setFormData(prevState => ({
                ...prevState,
                file: file,
            }));
        };
        if (file) {
            reader.readAsDataURL(file);
        }
    };

    const handleSubmit = async (event) => {
        event.preventDefault();
        if (formData.title.trim() === "") {
            return;
        }
        try{
            setLoading(true)
            const status = await loadTrack(formData)
            if (status===200){
                navigate(-1);
            } 
            console.log(status);
        }
        finally{
            setLoading(false)
        }
        console.log(formData);
        
    };

    return (
        <div className="playlist-form-container">
            <form onSubmit={handleSubmit}>
                <div className="form-group">
                    <label>Название трека:</label>
                    <input
                        type="text"
                        name="title"
                        value={formData.title}
                        onChange={handleChange}
                        required
                    />
                </div>
                <div className="form-group">
                    <label>Исполнитель:</label>
                    <input
                        type="text"
                        name="artist"
                        value={formData.artist}
                        onChange={handleChange}
                        required
                    />
                </div>
                <div className="form-group">
                    <label>Теги (через пробел):</label>
                    <textarea
                        name="tags"
                        value={formData.tags}
                        onChange={handleChange}
                    />
                </div>
                <div className="form-group">
                    <label>Трек:</label>
                    <input
                        type="file"
                        accept=".mp3,audio/*"
                        onChange={handleFileChange}
                        required
                    />
                </div>
                {!loading && <button className="submit-button" type="submit">Загрузить трек</button>}
                {loading && <LoaderIcon/>}
            </form>
        </div>
    );
};

export default LoadTrackForm;