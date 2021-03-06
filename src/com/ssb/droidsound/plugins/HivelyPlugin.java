package com.ssb.droidsound.plugins;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

import com.ssb.droidsound.file.FileSource;

public class HivelyPlugin extends DroidSoundPlugin {

	static {
		System.loadLibrary("hively");
	}
	
	private static String extension = "";
	
	public boolean checkHeader(FileSource fs)
	{
		FileInputStream filereader;
		try {
			filereader = new FileInputStream(fs.getFile().getPath());

	        byte [] fbuffer = new byte[4];
	         
	        filereader.read(fbuffer,0, 4);
	        filereader.close();
	        
			if (fbuffer[0] == 'H' && ((fbuffer[1] & 0xff) == 'V') && fbuffer[2] == 'L')
				return true;

			if (fbuffer[0] == 'T' && ((fbuffer[1] & 0xff) == 'H') && fbuffer[2] == 'X')
				return true;
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		return false;
        
        
	}
	
	@Override
	public String getVersion() {
		return "HVL Replay v1.6";
	}
	
		
	private long songRef;
	
	
	@Override
	public boolean canHandle(FileSource fs) {
		extension = fs.getExt().toUpperCase();
		if (extension.contains("HVL") || extension.contains("AHX"))
		{
			boolean isHVL = checkHeader(fs);
			if (!isHVL)
			{
				return false;
			}
		}

		return fs.getExt().equals("HVL") || fs.getExt().equals("AHX");
	}
	
	
	@Override
	public void getDetailedInfo(Map<String, Object> list) {
		
		list.put("plugin", "HIVELY");
		list.put("format", extension);
	}
	
	@Override
	public int getIntInfo(int what) {
		return 0;
	}

	@Override
	public int getSoundData(short[] dest, int size) {
		return N_getSoundData(songRef, dest, size);
	}

	@Override
	public String getStringInfo(int what) {
		return null;
	}

	@Override
	public boolean load(FileSource fs) {
		songRef = N_load(fs.getFile().getPath());
		return songRef != 0;
	}

	@Override
	public void unload() {
		N_unload(songRef);
	}

	native public long N_load(String filename);
	native public void N_unload(long song);
	native public int N_getSoundData(long song, short [] dest, int size);	
}
