import BackURL from "../urls";
import token from "../token";

async function createPlaylist(data) {
    try {
        const formData = new FormData();
        formData.append("image", data.image);
        const response = await fetch(BackURL + "/PlayList/Upload/", {
            method: 'POST',
            headers: {
                'token': token.getToken(),
                'ext': data.ext,
                'title': data.title,
                'description': data.description,
                'tracksid': "",
                'isprivat': data.isPrivate
            },
           
            body: formData
        });
        //console.log("Отправлен запрос на создание плейлиста");
        return response.status
    } catch (error) {
        console.error('Ошибка:', error);
        return null;
    }
}

export default createPlaylist