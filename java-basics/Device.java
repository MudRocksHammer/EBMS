
public class Device {

    private long id;
    private String name;
    private boolean available;

    public Device(long id, String name, boolean available) {
        this.id = id;
        this.name = name;
        this.available = available;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}
