# Project Description
This project is Video Streaming Project. User can use this app to share their videos to their friends. They can vote to pause, rewind the video as they want. If the vote is over half of people in the room, the action will be activate.

The user can also chat with each other.

That's all for this prototype.

# Technology
- JavaFx for the view
- UDP for streaming video (should focus on this the most)
- MySQL for database (this can be removed for now)

# In This Server
- Provide a Socket for UDP transfer used in video stream
- Provide a Socket for TCP transfer used in upload video
- Provide a Socket for TCP transfer used in chat
- Save the video in the server (**Why?** Because we want to watch together, with the least delay from the two parties)
- Must have Stream Segmenter to segment the video file into small parts (We don't need Media Encoder because user will upload the video file to the server, not record it live)

# Note
* We send stream of bytes not files
* The client can send the byte they need for the next chunk of bytes
* But this is the group livestream so there are many clients. If we let the clients to send the bytes they want, it will be a lot.
* Can we send a chunk of bytes after a period of time?
* Maybe. But it's not the best idea.
* Luckily, the JavaFX Media already has HLS (HTTP Live Stream) which will automatically download the media file on the Internet base on how the network working and show it to the user.
* HLS works perfectly with JavaFX, but only HTTP, not support HTTPS
* So the question is how to chunk video file to HLS protocol format?
* To do this, we must have [ffmpeg](https://ffmpeg.org/download.html) set up on our server

* To achieve great result for the new requirements, we should do research on the following resources
* (Vlcj-JavaFX) [https://github.com/caprica/vlcj-javafx]
* (Vlcj) [https://capricasoftware.co.uk/projects/vlcj]
* (JavaFX) [https://openjfx.io/]
* (Stream RTSP) [https://github.com/caprica/vlcj-examples/blob/master/src/main/java/uk/co/caprica/vlcj/test/streaming/StreamRtsp.java]
* (Why do RTSP) [https://www.remlab.net/op/vod.shtml]