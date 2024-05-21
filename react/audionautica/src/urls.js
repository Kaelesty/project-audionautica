class url {
    constructor() {
        this.url = null;
    }

    setUrl(newUrl) {
        this.url = newUrl;
    }
    getUrl() {
        return this.url;
    }
    toString() {
        return this.url
    }
}

const BackURL = new url();

export default BackURL;