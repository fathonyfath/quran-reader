package id.fathonyfath.quran.lite.useCase;

import id.fathonyfath.quran.lite.data.BookmarkRepository;
import id.fathonyfath.quran.lite.models.Bookmark;
import id.fathonyfath.quran.lite.utils.scheduler.Schedulers;

public class PutBookmarkUseCase extends BaseUseCase {

    private final BookmarkRepository bookmarkRepository;

    private UseCaseCallback<Boolean> callback;
    private Bookmark bookmark;

    public PutBookmarkUseCase(BookmarkRepository bookmarkRepository) {
        this.bookmarkRepository = bookmarkRepository;
    }

    public void setCallback(UseCaseCallback<Boolean> callback) {
        this.callback = callback;
    }

    public void setArguments(Bookmark bookmark) {
        this.bookmark = bookmark;
    }

    @Override
    protected void task() {
        Schedulers.IO().execute(new Runnable() {
            @Override
            public void run() {
                bookmarkRepository.putBookmark(bookmark);
                postResultToMainThread();
            }
        });
    }

    private void postResultToMainThread() {
        Schedulers.Main().execute(new Runnable() {
            @Override
            public void run() {
                if (callback != null) {
                    callback.onResult(true);
                }
            }
        });
    }

    public static class Factory implements UseCaseFactory<PutBookmarkUseCase> {

        private final BookmarkRepository bookmarkRepository;

        public Factory(BookmarkRepository bookmarkRepository) {
            this.bookmarkRepository = bookmarkRepository;
        }

        @Override
        public PutBookmarkUseCase create() {
            return new PutBookmarkUseCase(this.bookmarkRepository);
        }
    }
}
