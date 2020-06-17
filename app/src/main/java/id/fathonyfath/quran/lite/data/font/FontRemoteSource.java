package id.fathonyfath.quran.lite.data.font;

import java.io.File;

import id.fathonyfath.quran.lite.utils.network.NetworkHelper;

public class FontRemoteSource {
    private final String BASE_URL = "https://fathonyfath.github.io/external-resource/fonts/";

    public boolean downloadFont(File destination,
                                NetworkHelper.CancelSignal cancelSignal,
                                NetworkHelper.ProgressListener progressListener) {
        return NetworkHelper.doGetRequestAndSaveToFile(
                BASE_URL + "lpmq.otf",
                destination,
                cancelSignal,
                progressListener);
    }
}