package InformationClasses;

import java.util.UUID;

public class LibraryInfo {
    private UUID uuid;

    public LibraryInfo(){
        uuid = UUID.randomUUID();
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }
}
