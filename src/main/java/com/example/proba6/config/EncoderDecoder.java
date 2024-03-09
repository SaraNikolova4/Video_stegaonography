package com.example.proba6.config;

import com.example.proba6.Exception.Golemina;
import org.springframework.context.annotation.Configuration;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Random;

@Configuration
public class EncoderDecoder {

    public static int messageSize;
    public static String generateLargeStringMessage() {
        // Define a sample string to repeat
        String sampleString = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.";

        // Repeat the sample string to create a large message
        StringBuilder largeMessageBuilder = new StringBuilder();
        while (largeMessageBuilder.length() < 128 * 1024) { // 128KB
            largeMessageBuilder.append(sampleString);
        }

        return largeMessageBuilder.toString();
    }
    public static void encodeAudioData(String inputFilePath, String outputFilePath, String message) throws Golemina {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(inputFilePath));
            AudioFormat format = audioInputStream.getFormat();
            byte[] audioData = new byte[(int) audioInputStream.getFrameLength() * format.getFrameSize()];
            audioInputStream.read(audioData);
            audioInputStream.close();

            byte[] messageBytes = message.getBytes();
            messageSize = messageBytes.length;
            int availableSpace = audioData.length / 8; // Each byte holds 8 bits (1 bit per sample)
            if (messageSize > availableSpace) {
                throw new Golemina("Message size exceeds available space in audio file.");
            }
            for (int i = 0; i < messageSize; i++) {
                for (int j = 0; j < 8; j++) {
                    int index = i * 8 + j;
                    int mask = 1 << j;

                    if ((messageBytes[i] & mask) != 0) {
                        audioData[index] |= 1;
                    } else {
                        audioData[index] &= 0xFE;
                    }
                }
            }

            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(audioData);
            AudioInputStream encodedAudioInputStream = new AudioInputStream(byteArrayInputStream, format, audioData.length / format.getFrameSize());
            AudioSystem.write(encodedAudioInputStream, AudioFileFormat.Type.WAVE, new File(outputFilePath));
            encodedAudioInputStream.close();

            System.out.println("Audio encoding successful.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static String decodeAudioData(String filePath, int messageSize) {
        String decodedMessage = "Decode";
        try {
            AudioInputStream encodedAudioInputStream = AudioSystem.getAudioInputStream(new File(filePath));
            AudioFormat format = encodedAudioInputStream.getFormat();
            byte[] audioData = new byte[(int) encodedAudioInputStream.getFrameLength() * format.getFrameSize()];
            encodedAudioInputStream.read(audioData);
            encodedAudioInputStream.close();

            byte[] messageBytes = new byte[messageSize];

            for (int i = 0; i < messageSize; i++) {
                for (int j = 0; j < 8; j++) {
                    int index = i * 8 + j;
                    byte bit = (byte) (audioData[index] & 1);
                    messageBytes[i] |= (bit << j);
                }
            }

            decodedMessage = new String(messageBytes, StandardCharsets.UTF_8);
            System.out.println("Decoded message: " + decodedMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return decodedMessage;
    }

    }

