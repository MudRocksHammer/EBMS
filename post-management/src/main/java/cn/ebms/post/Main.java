package cn.ebms.post;

public class Main {

    public static void main(String[] args) {
        PostSaveReqVO post = new PostSaveReqVO();
        post.setName("Manager");
        post.setCode("MGR");
        post.setSort(1);
        post.setStatus(1);
        post.setRemark("Manager post");

        System.out.println(post.getName());
        System.out.println(post.getCode());
        System.out.println(post.getSort());
        System.out.println(post.getStatus());
        System.out.println(post.getRemark());

        PostDO postDO = new PostDO();
        // postDO.setId(1L);
        postDO.setName(post.getName());
        postDO.setCode(post.getCode());
        postDO.setSort(post.getSort());
        postDO.setStatus(post.getStatus());
        postDO.setRemark(post.getRemark());

        System.out.println(postDO.getId());
        System.out.println(postDO.getName());
        System.out.println(postDO.getCode());
        System.out.println(postDO.getSort());
        System.out.println(postDO.getStatus());
        System.out.println(postDO.getRemark());

        postDO.setName("Supervisor");
        System.out.println(postDO.getName());
        System.out.println(post.getName());

        PostRepository repository = new InMemoryPostRepository();
        repository.save(postDO);
        PostDO postDO_1 = new PostDO();
        postDO_1.setName("Counter");
        postDO_1.setCode("CTR");
        repository.save(postDO_1);
        System.out.println(postDO_1.getId());
        System.out.println(postDO.getId());

        PostDO foundByName = repository.findByName("Supervisor");
        System.out.println(foundByName != null ? foundByName.getId() : "Not found");
        PostDO foundByCode = repository.findByCode("MGR");
        System.out.println(foundByCode != null ? foundByCode.getId() : "Not found");

        PostDO foundByName_1 = repository.findByName("Counter");
        System.out.println(foundByName_1 != null ? foundByName_1.getId() : "Not found");
        PostDO foundByCode_1 = repository.findByCode("CTR");
        System.out.println(foundByCode_1 != null ? foundByCode_1.getId() : "Not found");

        PostDO foundByNameNotFound = repository.findByName("NonExistent");
        System.out.println(foundByNameNotFound != null ? foundByNameNotFound.getId() : "Not found");

        PostRepository serviceRepository = new InMemoryPostRepository();
        PostServiceImpl service = new PostServiceImpl(serviceRepository);

        System.out.println(service.createPost(post));
        System.out.println(serviceRepository.findByName(post.getName()).getId());

        try {
            PostSaveReqVO illegalPost1 = new PostSaveReqVO();
            illegalPost1.setName("   ");
            illegalPost1.setCode("ILG1");
            illegalPost1.setSort(1);
            illegalPost1.setStatus(1);
            service.createPost(illegalPost1);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }

        try {
            PostSaveReqVO illegalPost2 = new PostSaveReqVO();
            illegalPost2.setName("Illegal2");
            illegalPost2.setCode("ILG2");
            // no sort set
            illegalPost2.setStatus(1);
            service.createPost(illegalPost2);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }

        try {
            PostSaveReqVO illegalPost3 = new PostSaveReqVO();
            illegalPost3.setName("Illegal3");
            illegalPost3.setCode("ILG3");
            illegalPost3.setSort(1);
            illegalPost3.setStatus(4);
            service.createPost(illegalPost3);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }

        System.out.println(serviceRepository.findByCode("ILG1"));
        System.out.println(serviceRepository.findByCode("ILG2"));
        System.out.println(serviceRepository.findByCode("ILG3"));

        try {
            PostSaveReqVO duplicatePost = new PostSaveReqVO();
            duplicatePost.setName("Manager");
            duplicatePost.setCode("NEW1");
            duplicatePost.setSort(1);
            duplicatePost.setStatus(1);
            service.createPost(duplicatePost);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }

        try {
            PostSaveReqVO duplicatePostByCode = new PostSaveReqVO();
            duplicatePostByCode.setName("NewManager");
            duplicatePostByCode.setCode("MGR");
            duplicatePostByCode.setSort(1);
            duplicatePostByCode.setStatus(1);
            service.createPost(duplicatePostByCode);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }

        System.out.println(serviceRepository.findByCode("NEW1"));
        System.out.println(serviceRepository.findByName("NewManager"));
        System.out.println(serviceRepository.findByCode("MGR").getId());

        try {
            PostSaveReqVO Auditor = new PostSaveReqVO();
            Auditor.setName("Auditor");
            Auditor.setCode("AUD");
            Auditor.setSort(1);
            Auditor.setStatus(1);
            service.createPost(Auditor);

            System.out.println(serviceRepository.findByCode("AUD").getId());
            System.out.println(serviceRepository.findByName("Auditor").getId());
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
}
