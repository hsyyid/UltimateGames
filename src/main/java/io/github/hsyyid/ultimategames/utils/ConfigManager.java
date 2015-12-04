package io.github.hsyyid.ultimategames.utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ConfigManager
{
	public static String readJSON()
	{
		String json = null;

		try
		{
			json = readFile("UltimateGames.json", StandardCharsets.UTF_8);
		}
		catch (IOException e)
		{
			System.out.println("Could not read JSON file!");
		}

		return json;
	}
	
	public static void writeJSON(String json)
	{
		try
		{
			FileWriter fileWriter = new FileWriter("UltimateGames.json");
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
			bufferedWriter.write(json);
			bufferedWriter.flush();
			bufferedWriter.close();
		}
		catch (IOException ex)
		{
			System.out.println("Could not save JSON file!");
		}
	}
	
	private static String readFile(String path, Charset encoding) throws IOException
	{
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}
}
