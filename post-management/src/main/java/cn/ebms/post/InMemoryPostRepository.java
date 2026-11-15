package cn.ebms.post;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class InMemoryPostRepository implements PostRepository {

    private final Map<Long, PostDO> storage = new HashMap<>();
    private long nextId = 1L;

    @Override
    public void save(PostDO post) {
        if (post == null) {
            throw new IllegalArgumentException("Post cannot be null");
        }
        if (post.getId() != null) {
            throw new IllegalArgumentException("Post ID must be null");
        }
        post.setId(nextId++);
        storage.put(post.getId(), post);
    }

    @Override
    public PostDO findByName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Name cannot be null");
        }
        for (PostDO post : storage.values()) {
            if (Objects.equals(post.getName(), name)) {
                return post;
            }
        }
        return null;
    }

    @Override
    public PostDO findByCode(String code) {
        if (code == null) {
            throw new IllegalArgumentException("Code cannot be null");
        }
        for (PostDO post : storage.values()) {
            if (Objects.equals(post.getCode(), code)) {
                return post;
            }
        }
        return null;
    }

}
