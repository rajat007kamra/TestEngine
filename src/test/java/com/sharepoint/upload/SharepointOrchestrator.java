package com.sharepoint.upload;

import com.microsoft.graph.concurrency.ChunkedUploadProvider;
import com.microsoft.graph.concurrency.IProgressCallback;
import com.microsoft.graph.core.ClientException;
import com.microsoft.graph.models.extensions.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.log4j.Logger;

public class SharepointOrchestrator {
	final static Logger logger = Logger.getLogger(SharepointCredentials.class);
	private static final String filePath = "p20-legalentitymanagement-20210326.xlsx";
	private static final String siteId = "2de95b79-4299-4e48-ab40-ee5aa632c9fe";
	
	private InputStream getInputStream() throws FileNotFoundException {
        // Get an input stream for the file
//        InputStream fileStream = new FileInputStream("C:\\Users\\Public\\report_16.xlsx");
        
        //For linux
        File fileName = new File(filePath);
        InputStream fileStream = new FileInputStream(fileName.getAbsolutePath());
        return fileStream;
    }

    private long getStreamSize(InputStream fileStream) throws IOException {
        long streamSize = (long)fileStream.available();
        return streamSize;
    }

    // Create a callback used by the upload provider
    IProgressCallback<DriveItem> callback = new IProgressCallback<DriveItem>() {
        
        // Called after each slice of the file is uploaded
        public void progress(final long current, final long max) {
            logger.info(
                    String.format("Uploaded %d bytes of %d total bytes", current, max)
            );
        }

        public void success(final DriveItem result) {
            logger.info(
                    String.format("Uploaded file with ID: %s", result.id)
            );
        }

        public void failure(final ClientException ex) {
            logger.info(
                    String.format("Error uploading file: %s", ex.getMessage())
            );
        }
    };

    public void uploadDocument() throws IOException {
        final IGraphServiceClient graphClient = new GraphServiceProvider().getGraphServiceProvider();

        // upload to share point
        UploadSession uploadSession1 = graphClient
                .sites()
                .byId(siteId)
                .drive()
                .root()
                .itemWithPath(filePath)
                .createUploadSession(new DriveItemUploadableProperties())
                .buildRequest()
                .post();


        ChunkedUploadProvider<DriveItem> chunkedUploadProvider =
                new ChunkedUploadProvider<DriveItem>
                        (uploadSession1, graphClient, getInputStream(), getStreamSize(getInputStream()), DriveItem.class);

        // Config parameter is an array of integers
        // customConfig[0] indicates the max slice size
        // Max slice size must be a multiple of 320 KiB
        int[] customConfig = { 320 * 1024 };

        // Do the upload
        chunkedUploadProvider.upload(callback, customConfig);
    }

}