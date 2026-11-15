package cn.ebms.post.service;

import cn.ebms.post.model.dataobject.PostDO;
import cn.ebms.post.repository.PostRepository;
import cn.ebms.post.model.vo.PostSaveReqVO;

public class PostServiceImpl {

    private final PostRepository postRepository;

    public PostServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public Long createPost(PostSaveReqVO req) {
        validateRequest(req);
        validateUnique(req.getName(), req.getCode());
        PostDO postDO = new PostDO();
        postDO.setName(req.getName());
        postDO.setCode(req.getCode());
        postDO.setSort(req.getSort());
        postDO.setStatus(req.getStatus());
        postDO.setRemark(req.getRemark());

        postRepository.save(postDO);
        return postDO.getId();
    }

    private void validateRequest(PostSaveReqVO req) {
        if (req == null) {
            throw new IllegalArgumentException("Request cannot be null");
        }
        if (req.getName() == null || req.getName().trim().isEmpty() || req.getName().length() > 50) {
            throw new IllegalArgumentException("Name cannot be null, empty, or longer than 50 characters");
        }
        if (req.getCode() == null || req.getCode().trim().isEmpty() || req.getCode().length() > 64) {
            throw new IllegalArgumentException("Code cannot be null, empty, or longer than 64 characters");
        }

        if (req.getSort() == null) {
            throw new IllegalArgumentException("Sort cannot be null");
        }
        if (req.getStatus() == null || (req.getStatus() != 0 && req.getStatus() != 1)) {
            throw new IllegalArgumentException("Status cannot be null and must be 0 or 1");
        }
    }

    private void validateUnique(String name, String code) {
        PostDO post = postRepository.findByName(name);
        if (post != null) {
            throw new IllegalArgumentException("Name must be unique");
        }
        post = postRepository.findByCode(code);
        if (post != null) {
            throw new IllegalArgumentException("Code must be unique");
        }
    }
}
