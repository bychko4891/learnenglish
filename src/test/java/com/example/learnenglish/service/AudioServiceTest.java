package com.example.learnenglish.service;

/**
 * @author: Artur Hasparian
 * Application Name: Learn English
 * Description: Integration test
 * GitHub source code: https://github.com/bychko4891/learnenglish
 */
import com.example.learnenglish.model.Audio;
import com.example.learnenglish.property.FileStorageProperties;
import com.example.learnenglish.repository.AudioRepository;
import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.*;
import org.springframework.mock.web.MockMultipartFile;

import java.io.*;
import java.nio.file.*;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class AudioServiceTest {

    @Autowired
    private AudioService audioService;

    @Mock
    private AudioRepository audioRepository;

    @Mock
    private FileStorageProperties fileStorageProperties;

    @BeforeEach
    void setUp() throws IOException {
        MockitoAnnotations.openMocks(this);

        when(fileStorageProperties.getUploadAudio()).thenReturn("tempAudioDir");
        var tempDir = Paths.get("tempAudioDir");
        Files.createDirectories(tempDir);
        audioService = new AudioService(fileStorageProperties, audioRepository);
    }

    @Test
    void testGetWordAudio_ValidId_ReturnsAudio() {
        var audio = new Audio();
        audio.setId(1L);
        when(audioRepository.findById(1L)).thenReturn(Optional.of(audio));

        var result = audioService.getWordAudio(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void testGetWordAudio_InvalidId_ThrowsException() {
        when(audioRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> audioService.getWordAudio(1L));
    }

    @Test
    void testSaveAudioFile_ValidFiles_Success() throws IOException {
        Audio audio = new Audio();
        audio.setId(1L);
        when(audioRepository.findById(1L)).thenReturn(Optional.of(audio));

        var brAudio = new MockMultipartFile("brAudio.mp3", "brAudio.mp3", "audio/mpeg", "brAudioContent".getBytes());
        var usaAudio = new MockMultipartFile("usaAudio.mp3", "usaAudio.mp3", "audio/mpeg", "usaAudioContent".getBytes());

        var result = audioService.saveAudioFile(brAudio, usaAudio, 1L);

        assertEquals("yes", result);
    }

    @Test
    void testLoadFileAsResource_ValidFileName_ReturnsResource() {
        AudioService spyAudioService = spy(audioService);
        when(spyAudioService.loadFileAsResource("audio.mp3")).thenReturn(new ByteArrayResource("audioContent".getBytes()));

        Resource resource = spyAudioService.loadFileAsResource("audio.mp3");

        assertNotNull(resource);
    }
}


