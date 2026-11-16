package cn.ebms.post.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import cn.ebms.post.model.dataobject.PostDO;
import cn.ebms.post.model.vo.PostSaveReqVO;
import cn.ebms.post.repository.PostRepository;
import cn.ebms.post.repository.memory.InMemoryPostRepository;

class PostServiceImplTest {

    @Test
    void createPostAcceptsNameAtMaxLength() {
        PostRepository repository = new InMemoryPostRepository();
        PostServiceImpl service = new PostServiceImpl(repository);
        String name = "N".repeat(50);
        PostSaveReqVO request = validRequest(name, "MGR");

        Long id = service.createPost(request);

        assertEquals(1L, id);
        PostDO saved = repository.findByCode("MGR");
        assertNotNull(saved);
        assertEquals(id, saved.getId());
        assertEquals(name, saved.getName());
    }

    @Test
    void createPostRejectsNameExceedingMaxLength() {
        PostRepository repository = new InMemoryPostRepository();
        PostServiceImpl service = new PostServiceImpl(repository);
        PostSaveReqVO request = validRequest("N".repeat(51), "MGR");

        assertThrows(IllegalArgumentException.class,
                () -> service.createPost(request));

        assertNull(repository.findByCode("MGR"));
    }

    @Test
    void createPostAcceptsCodeAtMaxLength() {
        PostRepository repository = new InMemoryPostRepository();
        PostServiceImpl service = new PostServiceImpl(repository);
        String code = "C".repeat(64);
        PostSaveReqVO request = validRequest("Manager", code);

        Long id = service.createPost(request);

        assertEquals(1L, id);
        PostDO saved = repository.findByName("Manager");
        assertNotNull(saved);
        assertEquals(id, saved.getId());
        assertEquals(code, saved.getCode());
    }

    @Test
    void createPostRejectsCodeExceedingMaxLength() {
        PostRepository repository = new InMemoryPostRepository();
        PostServiceImpl service = new PostServiceImpl(repository);
        PostSaveReqVO request = validRequest("Manager", "C".repeat(65));

        assertThrows(IllegalArgumentException.class,
                () -> service.createPost(request));

        assertNull(repository.findByName("Manager"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "\t"})
    void createPostRejectsBlankCode(String code) {
        PostRepository repository = new InMemoryPostRepository();
        PostServiceImpl service = new PostServiceImpl(repository);
        PostSaveReqVO request = validRequest("Manager", code);

        assertThrows(IllegalArgumentException.class,
                () -> service.createPost(request));

        assertNull(repository.findByName("Manager"));
    }

    @Test
    void createPostRejectsNullRequest() {
        PostRepository repository = new InMemoryPostRepository();
        PostServiceImpl service = new PostServiceImpl(repository);

        assertThrows(IllegalArgumentException.class,
                () -> service.createPost(null));

        assertNull(repository.findByName("Manager"));
    }

    @Test
    void createPostRejectsMissingSort() {
        PostRepository repository = new InMemoryPostRepository();
        PostServiceImpl service = new PostServiceImpl(repository);
        PostSaveReqVO request = validRequest("Manager", "MGR");
        request.setSort(null);

        assertThrows(IllegalArgumentException.class,
                () -> service.createPost(request));

        assertNull(repository.findByName("Manager"));
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(ints = {-1, 2, 4})
    void createPostRejectsInvalidStatus(Integer status) {
        PostRepository repository = new InMemoryPostRepository();
        PostServiceImpl service = new PostServiceImpl(repository);
        PostSaveReqVO request = validRequest("Manager", "MGR");
        request.setStatus(status);

        assertThrows(IllegalArgumentException.class,
                () -> service.createPost(request));

        assertNull(repository.findByName("Manager"));
    }

    private PostSaveReqVO validRequest(String name, String code) {
        PostSaveReqVO request = new PostSaveReqVO();
        request.setName(name);
        request.setCode(code);
        request.setSort(1);
        request.setStatus(0);
        request.setRemark("Boundary test");
        return request;
    }

    @Test
    void createPostSavesValidPost() {
        PostRepository serviceRepository = new InMemoryPostRepository();
        PostServiceImpl service = new PostServiceImpl(serviceRepository);

        PostSaveReqVO postVO = new PostSaveReqVO();
        postVO.setName("Manager");
        postVO.setCode("MGR");
        postVO.setSort(1);
        postVO.setStatus(1);
        postVO.setRemark("Manager post");
        Long id = service.createPost(postVO);

        assertNotNull(id);
        assertEquals(1L, id);
        assertEquals("Manager", serviceRepository.findByName("Manager").getName());
        assertEquals("MGR", serviceRepository.findByName("Manager").getCode());
    }

    @Test
    void createPostRejectDuplicateName() {
        PostRepository serviceRepository = new InMemoryPostRepository();
        PostServiceImpl service = new PostServiceImpl(serviceRepository);

        PostSaveReqVO postVO = new PostSaveReqVO();
        postVO.setName("Manager");
        postVO.setCode("MGR");
        postVO.setSort(1);
        postVO.setStatus(1);
        postVO.setRemark("Manager post");
        service.createPost(postVO);

        PostSaveReqVO duplicatePostVO = new PostSaveReqVO();
        duplicatePostVO.setName("Manager");
        duplicatePostVO.setCode("MGR2");
        duplicatePostVO.setSort(2);
        duplicatePostVO.setStatus(1);
        duplicatePostVO.setRemark("Duplicate manager post");

        assertThrows(IllegalArgumentException.class,
             () -> service.createPost(duplicatePostVO));
    
        assertNull(serviceRepository.findByCode("MGR2"));
        PostDO existingPostDO = serviceRepository.findByName("Manager");
        assertNotNull(existingPostDO);
        assertEquals(1L, existingPostDO.getId());
        assertEquals("Manager", existingPostDO.getName());
        assertEquals("MGR", existingPostDO.getCode());
    }

    @Test
    void createPostRejectDuplicateCode() {
        PostRepository serviceRepository = new InMemoryPostRepository();
        PostServiceImpl service = new PostServiceImpl(serviceRepository);

        PostSaveReqVO postVO = new PostSaveReqVO();
        postVO.setName("Manager");
        postVO.setCode("MGR");
        postVO.setSort(1);
        postVO.setStatus(1);
        postVO.setRemark("Manager post");
        service.createPost(postVO);

        PostSaveReqVO duplicatePostVO = new PostSaveReqVO();
        duplicatePostVO.setName("Manager2");
        duplicatePostVO.setCode("MGR");
        duplicatePostVO.setSort(2);
        duplicatePostVO.setStatus(1);
        duplicatePostVO.setRemark("Duplicate manager post");

        assertThrows(IllegalArgumentException.class,
             () -> service.createPost(duplicatePostVO));

        assertNull(serviceRepository.findByName("Manager2"));
        PostDO existingPostDO = serviceRepository.findByCode("MGR");
        assertNotNull(existingPostDO);
        assertEquals(1L, existingPostDO.getId());
        assertEquals("Manager", existingPostDO.getName());
        assertEquals("MGR", existingPostDO.getCode());
    }

    @ParameterizedTest 
    @NullAndEmptySource 
    @ValueSource(strings = {"   ", "\t"})
    void createPostRejectBlankName(String name){
        PostRepository serviceRepository = new InMemoryPostRepository();
        PostServiceImpl service = new PostServiceImpl(serviceRepository);

        PostSaveReqVO postVO = new PostSaveReqVO();
        postVO.setName(name);
        postVO.setCode("MGR");
        postVO.setSort(1);
        postVO.setStatus(1);
        postVO.setRemark("Manager post");

        assertThrows(IllegalArgumentException.class,
             () -> service.createPost(postVO));

        assertNull(serviceRepository.findByCode("MGR"));
    }
}
