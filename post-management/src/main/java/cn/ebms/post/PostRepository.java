package cn.ebms.post;

public interface PostRepository {

    PostDO findByName(String name);

    PostDO findByCode(String code);

    void save(PostDO post);
}
