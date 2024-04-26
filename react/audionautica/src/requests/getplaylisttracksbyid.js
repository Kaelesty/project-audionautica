import BackURL from "../urls";
import token from "../token";

async function getPlaylistTracksById(playlist_id) {
    try {
        const response = await fetch(BackURL + "/PlayLst/PlayListTracksInfoId/", {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'token': token.getToken()
            },
            body: JSON.stringify({playlist_id}),
        
        });
        
        if (response.status === 404) {
            console.error('Плейлист не найден');
            return null;
        }
        
        console.log("Отправлен запрос на получение треков плейлиста");
        const data = await response.json();
        return data.tracks // {tracks: Array(6)}
    } catch (error) {
        console.error('Ошибка:', error);
        return null;
    }
}

export default getPlaylistTracksById