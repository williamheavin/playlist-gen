/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.william.playlistgen;

import com.william.dev.common.utils.Song;
import com.william.playlistgen.database.LibraryDao;
import com.william.playlistgen.exception.DirectoryNotFoundException;
import com.william.playlistgen.file.MusicLibraryScanner;
import com.william.playlistgen.file.PlaylistFileWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;

/**
 * @author William
 */
public class PlaylistGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(PlaylistGenerator.class);
    private final static String PLAYLIST_FILE_EXT = ".m3u";
    final LibraryDao libraryDao;
    private final String playlistDirectory;

    public PlaylistGenerator(final String playlistDirectory) {
        libraryDao = LibraryDao.getInstance();
        this.playlistDirectory = playlistDirectory;
    }

    public void loadLibrary(final String musicLibraryPath) {
        try {
            final MusicLibraryScanner libraryScanner = new MusicLibraryScanner(musicLibraryPath);
            libraryScanner.startScan();
        } catch (final DirectoryNotFoundException ex) {
            LOGGER.error("Error loading music library [{}]", musicLibraryPath, ex);
        }
    }

    void generatePlaylistFile(final List<Song> songList, final String playlistFileName) {
        final String playlistFile = playlistFileName + PLAYLIST_FILE_EXT;
        if (!songList.isEmpty()) {
            final String filePath = playlistDirectory + File.separator + playlistFile;
            final PlaylistFileWriter playlistFileWriter = new PlaylistFileWriter(filePath);
            playlistFileWriter.writePlaylistToFile(songList);
            LOGGER.info("Playlist written to [{}]", playlistFile);
        } else {
            LOGGER.warn("No matching songs found. Playlist not generated");
        }
    }
}
