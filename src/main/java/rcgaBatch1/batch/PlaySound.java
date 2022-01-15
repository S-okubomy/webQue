package rcgaBatch1.batch;

import java.io.File;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;


public class PlaySound {

	// WAVEファイル名
    private static final String[] waveNames = {"attack.wav", "spell.wav", "escape.wav"};

	public static void main(String[] args) {

		//以下のファイル名を適宜変更すること。

        AudioFormat format = null;
        DataLine.Info info = null;
        Clip line = null;
        File audioFile = null;

        try{

            audioFile = new File("C:\\pleiades\\workspace\\getStudyMusicv1\\src\\main\\java\\rcgaBatch1\\batch\\wave\\attack.wav");
            format = AudioSystem.getAudioFileFormat(audioFile).getFormat();
            info = new DataLine.Info(Clip.class, format);
            line = (Clip)AudioSystem.getLine(info);
            line.open(AudioSystem.getAudioInputStream(audioFile));
            line.start();
        }
        catch(Exception e){
            e.printStackTrace();
        }
		System.exit(0);
	}


}
