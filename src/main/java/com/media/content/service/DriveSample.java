//package com.media.metadata.service;
//
//import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
//import com.google.api.client.http.GenericUrl;
//import com.google.api.client.http.HttpResponse;
//import com.google.api.client.http.HttpTransport;
//import com.google.api.client.json.JsonFactory;
//import com.google.api.client.json.jackson2.JacksonFactory;
//import com.google.api.client.util.store.FileDataStoreFactory;
//import com.google.api.services.drive.Drive;
//import com.google.api.services.drive.DriveScopes;
//import com.google.api.services.drive.model.File;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.Arrays;
//import java.util.List;
//
///**
// * Created by mparamasivam on 29/07/17.
// */
//
//public class DriveSample {
//    /** Application name. */
//    private static final String APPLICATION_NAME = "Drive API Java Quickstart";
//
//    /** Directory to store user credentials for this application. */
//    private static final java.io.File DATA_STORE_DIR = new java.io.File(System.getProperty("user.home"),
//            ".credentials/drive-java-quickstart");
//
//    /** Global instance of the {@link FileDataStoreFactory}. */
//    private static FileDataStoreFactory DATA_STORE_FACTORY;
//
//    /** Global instance of the JSON factory. */
//    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
//
//    /** Global instance of the HTTP transport. */
//    private static HttpTransport HTTP_TRANSPORT;
//
//    /**
//     * Global instance of the scopes required by this quickstart.
//     *
//     * If modifying these scopes, delete your previously saved credentials at
//     * ~/.credentials/drive-java-quickstart
//     */
//    private static final List<String> SCOPES = Arrays.asList(DriveScopes.DRIVE_METADATA_READONLY);
//
//    static {
//        try {
//            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
//            DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
//        } catch (Throwable t) {
//            t.printStackTrace();
//            System.exit(1);
//        }
//    }
//
////    public static Credential authorize() throws IOException {
////        // Load client secrets.
////        InputStream in = Quickstart.class.getResourceAsStream("/client_secret.json");
////        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
////
////        // Build flow and trigger user authorization request.
////        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY,
////                clientSecrets, SCOPES).setDataStoreFactory(DATA_STORE_FACTORY).setAccessType("offline").build();
////        Credential credential = new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
////        System.out.println("Credentials saved to " + DATA_STORE_DIR.getAbsolutePath());
////        return credential;
////    }
//
//    /**
//     * Build and return an authorized Drive client service.
//     *
//     * @return an authorized Drive client service
//     * @throws IOException
//     */
//    public static Drive getDriveService() throws IOException {
//        return new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, null).setApplicationName(APPLICATION_NAME).build();
//    }
//
//    private static void printFile(Drive service, String fileId) {
//
//        try {
//            File file = service.files().get(fileId).execute();
//
//            System.out.println("Title: " + file.getTitle());
//            System.out.println("Description: " + file.getDescription());
//            System.out.println("MIME type: " + file.getMimeType());
//        } catch (IOException e) {
//            System.out.println("An error occurred: " + e);
//        }
//    }
//
//    /**
//     * Download a file's content.
//     *
//     * @param service Drive API service instance.
//     * @param file Drive File instance.
//     * @return InputStream containing the file's content if successful,
//     *         {@code null} otherwise.
//     */
//    private static InputStream downloadFile(Drive service, File file) {
//        if (file.getDownloadUrl() != null && file.getDownloadUrl().length() > 0) {
//            try {
//                HttpResponse resp =
//                        service.getRequestFactory().buildGetRequest(new GenericUrl(file.getDownloadUrl()))
//                                .execute();
//                return resp.getContent();
//            } catch (IOException e) {
//                // An error occurred.
//                e.printStackTrace();
//                return null;
//            }
//        } else {
//            // The file doesn't have any content stored on Drive.
//            return null;
//        }
//    }
//
//    public static void main(String[] args) throws IOException {
//        // Build a new authorized API client service.
//        Drive service = getDriveService();
//        printFile(service, "0BxVV3wqQ8NkLNE1wX0lubWZYSWM");
//
//    }
//}
