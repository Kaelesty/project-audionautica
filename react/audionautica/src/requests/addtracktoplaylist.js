import BackURL from "../urls";
import token from "../token";

async function addTrackToPlaylist(trackId, playlistId) {
    const requestData = {
        "playlist_id": playlistId,
        "track_id": trackId
    };
    try {
        const response = await fetch(BackURL + "/PlayList/AddTrackToPlayList/", {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'token': token.getToken()
            },
            body: JSON.stringify(requestData)   
        });

        if (response.status === 404) {
            console.error('Плейлист не найден');
            return null;
        }

        //console.log("Отправлен запрос на добавление трека в плейлист с id", playlistId);

    } catch (error) {
        console.error('Ошибка:', error);
        return null;
    }
}

export default addTrackToPlaylist