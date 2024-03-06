class Token {
    constructor() {
        this.Token = null;
    }

    setToken(newToken) {
        this.Token = newToken;
    }
    removeToken(){
        this.Token = null;
    }
    getToken() {
        return this.Token;
    }

  
}

const token = new Token();

export default token;