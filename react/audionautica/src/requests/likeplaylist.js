import BackURL from "../urls";
import token from "../token";

async function likePlaylist(playlist_id) {
    try {
        const response = await fetch(BackURL + "/PlayList/LikePlayList/", {
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
        
        console.log("Лайк плейлиста ", playlist_id);
    } catch (error) {
        console.error('Ошибка:', error);
        return null;
    }
}

export default likePlaylist