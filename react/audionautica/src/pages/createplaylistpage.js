import React, { useState } from "react";
import "./createplaylistpage.css"
import { useNavigate } from "react-router-dom"; 
import createPlaylist from "../requests/createplaylist";
import LoaderIcon from "../components/search/download";

const PlaylistForm = () => {
    const [formData, setFormData] = useState({
        title: "",
        description: "",
        image: "",
        ext: "",
        isPrivate: true
    });
    const navigate = useNavigate(); 
    const [previewPic, setPreviewPic] = useState()
    const [loading, setLoading] = useState(false)

    const handleChange = (event) => {
        const { name, value, type, checked } = event.target;
        setFormData(prevState => ({
            ...prevState,
            [name]: type === "checkbox" ? checked : value
        }));
    };

    const handleImageChange = (event) => {
        const file = event.target.files[0];
        const reader = new FileReader();
        const fileName = file.name; 
        const fileNameParts = fileName.split("."); 
        const extension = "." + fileNameParts[fileNameParts.length - 1]; 
        reader.onloadend = () => {
            setPreviewPic(reader.result)
            setFormData(prevState => ({
                ...prevState,
                image: file,
                ext: extension
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
            const status = await createPlaylist(formData)
            console.log(formData);
            if (status===200){
                navigate(-1);
            } 
            console.log(status);
        }
        finally{
            setLoading(false)
        }
        
    };

    return (
        <div className="playlist-form-container">
            <form onSubmit={handleSubmit}>
                <div className="form-group">
                    <label>Название плейлиста:</label>
                    <input
                        type="text"
                        name="title"
                        value={formData.title}
                        onChange={handleChange}
                        required
                    />
                </div>
                <div className="form-group">
                    <label>Описание:</label>
                    <textarea
                        name="description"
                        value={formData.description}
                        onChange={handleChange}
                    />
                </div>
                <div className="form-group">
                    <label>Обложка:</label>
                    <input
                        type="file"
                        accept="image/*"
                        onChange={handleImageChange}
                        required
                    />
                    {formData.image && (
                        <img src={previewPic} alt="Preview" className="image-preview" />
                    )}
                </div>
                <div className="form-group">
                    <label>
                        Приватный плейлист:
                        <input
                            type="checkbox"
                            name="isPrivate"
                            checked={formData.isPrivate}
                            onChange={handleChange}
                        />
                    </label>
                </div>
                {!loading && <button className="submit-button" type="submit">Создать плейлист</button>}
                {loading && <LoaderIcon/>}
            </form>
        </div>
    );
};

export default PlaylistForm;