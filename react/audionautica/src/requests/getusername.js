import BackURL from "../urls";
import token from "../token";

async function getUsername() {
    try {
        const response = await fetch(BackURL + "/Auth/User/", {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'token': token.getToken()
            },
           
        });
        
        if (response.status === 404) {
            console.error('Пользователь не найден');
            return null;
        }
        console.log("Запрос на получение данных пользователя");
        const result = await response.json()
        return result.name
    } catch (error) {
        console.error('Ошибка:', error);
        return null;
    }
}

export default getUsername