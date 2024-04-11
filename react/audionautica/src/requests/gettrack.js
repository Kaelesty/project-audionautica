import BackURL from "../urls";
import token from "../token";

async function getTrackById(id, currentTrack, setCurrentTrack, abort, setAbort) {
    if (abort){
      abort.abort()      
    }
    try {
      const newController = new AbortController();
      setAbort(newController)
      const mediaSource = new MediaSource();
      
      const audio = new Audio();
      audio.src = URL.createObjectURL(mediaSource);
      
      await new Promise((resolve, reject) => {
        mediaSource.addEventListener('sourceopen', () => {
          resolve();
        });
        mediaSource.addEventListener('error', (e) => {
          reject(e);
        });
      });
  
      const sourceBuffer = mediaSource.addSourceBuffer('audio/mpeg');

      const { signal } = newController;
      
      const response = await fetch(BackURL + "/Music/GetTrack/", {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'token': token.getToken()
        },
        body: JSON.stringify({ id }),
        signal,
      });
  
      if (!response.ok) {
        throw new Error('Failed to fetch track');
      }
  
      const reader = response.body.getReader();
      const totalSize = +response.headers.get('Content-Length');
      const initialChunkSize = 1024*10;
      let loaded = 0;

      let firstChankLoaded = false
      let aborted = false
      signal.addEventListener('abort', () => {
        reader.cancel();
        sourceBuffer.abort();
        aborted = true
        console.log("Loading aborted");

      });

      const appendData = async () => {
        
        const { done, value } = await reader.read();

        if (done) {
          console.log('Track loaded, duration', audio.duration );
          mediaSource.endOfStream();
          if (!aborted){
            setAbort(null)
          }
          return;
        }
  
        await new Promise((resolve) => {
          sourceBuffer.addEventListener('updateend', () => {
            resolve();
          }, { once: true });
          sourceBuffer.appendBuffer(value);
        });
  
        loaded += value.length;

        if (loaded < initialChunkSize) {
          appendData();
        } 
        else if (!firstChankLoaded) {
          if (currentTrack){          
            currentTrack.pause()
          }
          audio.play();
          console.log("first chunk appended, progress ", loaded / totalSize)
          setCurrentTrack(audio)
          
          appendData();
          firstChankLoaded = true
        }
        else{
          appendData();
          console.log("data appended, progress ", loaded / totalSize, abort)
        }
      };
      
      appendData();
    } catch (error) {
      if (error.name === 'AbortError') {
        console.log('Request aborted');
      } else {
        console.error('Error handling track click:', error);
      }
    }
  };

export default getTrackById