import BackURL from "../urls";
import token from "../token";

async function getPlaylistById(id) {
    try {
        const response = await fetch(BackURL + "/PlayList/PlayListInfoId/", {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'token': token.getToken()
            },
            body: JSON.stringify({id}),
        
        });
        
        if (response.status === 404) {
            console.error('Плейлист не найден');
            return null;
        }
        
        console.log("Отправлен запрос на получение плейлиста");
        const data = await response.json();
        return data // Object{ title: "Favorite Playlist", creatorid: "10", description: "", tracksid: "", liked: "0", isprivat: "True" }
    } catch (error) {
        console.error('Ошибка:', error);
        return null;
    }
}

export default getPlaylistById