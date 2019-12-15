package InformationClasses;

import java.util.UUID;

public abstract class LibraryInfo {
    private UUID uuid;

    public LibraryInfo(){
        uuid = UUID.randomUUID();
    }

    public abstract  Book getBook();

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }
}
