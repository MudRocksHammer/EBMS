package cn.ebms.post.repository;

import cn.ebms.post.model.dataobject.PostDO;

public interface PostRepository {

    PostDO findByName(String name);

    PostDO findByCode(String code);

    void save(PostDO post);
}
