class TrackQueue {
  constructor() {
    this.queue = [];
    this.current = null;
    this.currentTrack = {};
  }

  clear() {
    this.queue = [];
  }

  addTrack(id, title, artist) {
    this.queue.push({ id, title, artist });
      if (this.queue.length === 1){
        this.current = 0;
        this.currentTrack = this.queue[this.current];
    }
  }  

  setTrack(id, title, artist){
    this.clear();
    this.queue.push({ id, title, artist });
    this.current = 0;
    this.currentTrack = this.queue[this.current];
  }

  nextTrack(replayMode){
    if (replayMode === 1){
      this.current = (this.current + 1) % this.queue.length
    }
    else {
      if (this.current < this.currentTrack){
        this.current += 1
      }
    }

    this.currentTrack = this.queue[this.current]
  }
}

const queue = new TrackQueue()
export default queue