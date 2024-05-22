import BackURL from "../urls";
import token from "../token";

async function loadTrack(data) {
    try {
        const formData = new FormData();
        formData.append("audio", data.file);
        const formatTags = data.tags.split(" ").join("/")
        const response = await fetch(BackURL + "/Music/Upload/", {
            method: 'POST',
            headers: {
                'token': token.getToken(),
                'title': data.title,
                'artist': data.artist,
                'tags': formatTags,
            },
            body: formData
        });
        //console.log("Отправлен запрос на загрузку трека");
        return response.status
    } catch (error) {
        console.error('Ошибка:', error);
        return null;
    }
}

export default loadTrack