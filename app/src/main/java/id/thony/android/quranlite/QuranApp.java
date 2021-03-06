package id.thony.android.quranlite;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import id.thony.android.quranlite.data.BookmarkRepository;
import id.thony.android.quranlite.data.ConfigRepository;
import id.thony.android.quranlite.data.FontProvider;
import id.thony.android.quranlite.data.QuranRepository;
import id.thony.android.quranlite.data.SearchIndexRepository;
import id.thony.android.quranlite.data.font.FontRemoteSource;
import id.thony.android.quranlite.data.source.disk.QuranDiskSource;
import id.thony.android.quranlite.data.source.disk.SearchIndexDiskSource;
import id.thony.android.quranlite.data.source.network.QuranNetworkSource;
import id.thony.android.quranlite.data.source.preferences.BookmarkPreferencesSource;
import id.thony.android.quranlite.data.source.preferences.DayNightPreferencesSource;
import id.thony.android.quranlite.useCase.DoSearchUseCase;
import id.thony.android.quranlite.useCase.FetchAllSurahDetailUseCase;
import id.thony.android.quranlite.useCase.FetchAllSurahUseCase;
import id.thony.android.quranlite.useCase.FetchSurahDetailUseCase;
import id.thony.android.quranlite.useCase.GetBookmarkUseCase;
import id.thony.android.quranlite.useCase.GetDayNightPreferenceUseCase;
import id.thony.android.quranlite.useCase.GetDayNightUseCase;
import id.thony.android.quranlite.useCase.InstallFontIfNecessaryUseCase;
import id.thony.android.quranlite.useCase.PutBookmarkUseCase;
import id.thony.android.quranlite.useCase.PutDayNightPreferenceUseCase;
import id.thony.android.quranlite.useCase.UseCaseProvider;

public class QuranApp extends Application {

    public static final String QURAN_REPOSITORY_SERVICE = "MainActivity.QuranRepository";
    public static final String FONT_PROVIDER_SERVICE = "MainActivity.FontProvider";
    public static final String BOOKMARK_REPOSITORY_SERVICE = "MainActivity.BookmarkRepository";
    public static final String CONFIG_REPOSITORY_SERVICE = "MainActivity.ConfigRepository";
    public static final String SEARCH_INDEX_REPOSITORY_SERVICE = "MainActivity.SearchIndexRepository";

    private QuranRepository quranRepository;
    private FontProvider fontProvider;
    private BookmarkRepository bookmarkRepository;
    private ConfigRepository configRepository;
    private SearchIndexRepository searchIndexRepository;

    @Override
    public void onCreate() {
        super.onCreate();

        initService();
        registerUseCaseFactory();
    }

    @Override
    public Object getSystemService(String name) {
        if (name.equals(QURAN_REPOSITORY_SERVICE)) {
            return quranRepository;
        } else if (name.equals(FONT_PROVIDER_SERVICE)) {
            return fontProvider;
        } else if (name.equals(BOOKMARK_REPOSITORY_SERVICE)) {
            return bookmarkRepository;
        } else if (name.equals(CONFIG_REPOSITORY_SERVICE)) {
            return configRepository;
        } else if (name.equals(SEARCH_INDEX_REPOSITORY_SERVICE)) {
            return searchIndexRepository;
        }
        return super.getSystemService(name);
    }

    private void initService() {
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        QuranDiskSource quranDiskSource = new QuranDiskSource(this.getApplicationContext());
        QuranNetworkSource quranNetworkSource = new QuranNetworkSource();
        FontRemoteSource fontRemoteSource = new FontRemoteSource();
        BookmarkPreferencesSource bookmarkPreferencesSource = new BookmarkPreferencesSource(defaultSharedPreferences);
        DayNightPreferencesSource dayNightPreferencesSource = new DayNightPreferencesSource(defaultSharedPreferences);
        SearchIndexDiskSource searchIndexDiskSource = new SearchIndexDiskSource(this.getApplicationContext());

        this.quranRepository = new QuranRepository(quranDiskSource, quranNetworkSource);
        this.fontProvider = new FontProvider(this.getApplicationContext(), fontRemoteSource);
        this.bookmarkRepository = new BookmarkRepository(bookmarkPreferencesSource);
        this.configRepository = new ConfigRepository(dayNightPreferencesSource);
        this.searchIndexRepository = new SearchIndexRepository(searchIndexDiskSource);
    }

    private void registerUseCaseFactory() {
        UseCaseProvider.registerFactory(InstallFontIfNecessaryUseCase.class, new InstallFontIfNecessaryUseCase.Factory(this.fontProvider));
        UseCaseProvider.registerFactory(FetchAllSurahUseCase.class, new FetchAllSurahUseCase.Factory(this.quranRepository));
        UseCaseProvider.registerFactory(FetchSurahDetailUseCase.class, new FetchSurahDetailUseCase.Factory(this.quranRepository));
        UseCaseProvider.registerFactory(GetBookmarkUseCase.class, new GetBookmarkUseCase.Factory(this.bookmarkRepository));
        UseCaseProvider.registerFactory(PutBookmarkUseCase.class, new PutBookmarkUseCase.Factory(this.bookmarkRepository));
        UseCaseProvider.registerFactory(GetDayNightUseCase.class, new GetDayNightUseCase.Factory(this, this.configRepository));
        UseCaseProvider.registerFactory(GetDayNightPreferenceUseCase.class, new GetDayNightPreferenceUseCase.Factory(this.configRepository));
        UseCaseProvider.registerFactory(PutDayNightPreferenceUseCase.class, new PutDayNightPreferenceUseCase.Factory(this, this.configRepository));
        UseCaseProvider.registerFactory(DoSearchUseCase.class, new DoSearchUseCase.Factory(this.quranRepository, this.searchIndexRepository));
        UseCaseProvider.registerFactory(FetchAllSurahDetailUseCase.class, new FetchAllSurahDetailUseCase.Factory(this.quranRepository));
    }
}
