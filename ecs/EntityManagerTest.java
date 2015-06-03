package ecs;

import org.junit.Test;

import java.util.HashSet;
import java.util.UUID;

public class EntityManagerTest {
    private static class TestComponent implements Component {}
    private static class TestComponent2 implements Component {}
    private static class TestComponent3 implements Component {}

    @Test
    public void testCreateEntity() throws Exception {
        EntityManager entityManager = new EntityManager();
        UUID entity = UUID.randomUUID();
        assert (!entityManager.getAllEntitiesSetDirectly().contains(entity));
        entity = entityManager.createEntity();
        assert (entityManager.getAllEntitiesSetDirectly().contains(entity));
        assert (entityManager.getEntityComponentMapDirectly().isEmpty());
        assert (entityManager.getComponentMapByClassDirectly().isEmpty());
        assert (entityManager.getEntityNameMapDirectly().isEmpty());
    }

    @Test
    public void testCreateEntityWithName() throws Exception {
        EntityManager entityManager = new EntityManager();
        UUID entity = UUID.randomUUID();
        assert (!entityManager.getAllEntitiesSetDirectly().contains(entity));
        entity = entityManager.createEntity("test");
        assert (entityManager.getAllEntitiesSetDirectly().contains(entity));
        assert (entityManager.getEntityComponentMapDirectly().isEmpty());
        assert (entityManager.getComponentMapByClassDirectly().isEmpty());
        assert (entityManager.getEntityNameMapDirectly().containsKey(entity));
        assert (entityManager.getEntityNameMapDirectly().get(entity).equals("test"));
    }

    @Test
    public void testAddEntity() throws Exception {
        EntityManager entityManager1 = new EntityManager();
        EntityManager entityManager2 = new EntityManager();
        UUID entity = entityManager1.createEntity("entity");
        assert (entityManager1.entityExists(entity));
        assert (!entityManager2.entityExists(entity));
        entityManager2.addEntity(entity, "entity′");
        assert (entityManager1.entityExists(entity));
        assert (entityManager2.entityExists(entity));
        assert (entityManager1.getEntityName(entity).equals("entity"));
        assert (entityManager2.getEntityName(entity).equals("entity′"));
        entityManager1.killEntity(entity);
        assert (!entityManager1.entityExists(entity));
        assert (entityManager2.entityExists(entity));
        assert (!entityManager2.addEntity(entityManager2.createEntity(), "new entity"));
    }

    @Test
    public void testAddEntityWithName() throws Exception {
        EntityManager entityManager = new EntityManager();
        UUID entity = UUID.randomUUID();
        assert (!entityManager.getAllEntitiesSetDirectly().contains(entity));
        entity = entityManager.createEntity("test");
        assert (entityManager.getAllEntitiesSetDirectly().contains(entity));
        assert (entityManager.getEntityComponentMapDirectly().isEmpty());
        assert (entityManager.getComponentMapByClassDirectly().isEmpty());
        assert (entityManager.getEntityNameMapDirectly().containsKey(entity));
        assert (entityManager.getEntityNameMapDirectly().get(entity).equals("test"));
    }

    @Test
    public void testKillEntity() throws Exception {
        EntityManager entityManager = new EntityManager();
        UUID entity = entityManager.createEntity("test");
        assert (entityManager.entityExists(entity));
        entityManager.killEntity(entity);
        assert (!entityManager.entityExists(entity));
        assert (entityManager.getAllEntitiesSetDirectly().isEmpty());
        assert (entityManager.getEntityComponentMapDirectly().isEmpty());
        assert (entityManager.getComponentMapByClassDirectly().isEmpty());
        assert (entityManager.getEntityNameMapDirectly().isEmpty());
    }

    @Test
    public void testKillEntityWithComponents() throws Exception {
        EntityManager entityManager = new EntityManager();
        UUID entity = entityManager.createEntity("test");
        TestComponent testComponent = new TestComponent();
        TestComponent2 testComponent2 = new TestComponent2();
        TestComponent3 testComponent3 = new TestComponent3();
        assert (entityManager.entityExists(entity));
        entityManager.addComponent(entity, testComponent);
        entityManager.killEntity(entity);
        assert (!entityManager.entityExists(entity));
        assert (entityManager.getAllEntitiesSetDirectly().isEmpty());
        assert (entityManager.getEntityComponentMapDirectly().isEmpty());
        assert (entityManager.getComponentMapByClassDirectly().isEmpty());
        assert (entityManager.getEntityNameMapDirectly().isEmpty());

        entity = entityManager.createEntity("test");
        entityManager.addComponent(entity, testComponent);
        entityManager.addComponent(entity, testComponent2);
        entityManager.addComponent(entity, testComponent3);
        entityManager.killEntity(entity);
        assert (!entityManager.entityExists(entity));
        assert (entityManager.getAllEntitiesSetDirectly().isEmpty());
        assert (entityManager.getEntityComponentMapDirectly().isEmpty());
        assert (entityManager.getComponentMapByClassDirectly().isEmpty());
        assert (entityManager.getEntityNameMapDirectly().isEmpty());
        assert (entityManager.getEntityComponentMapDirectly().isEmpty());
    }

    @Test
    public void testEntityExists() throws Exception {
        EntityManager entityManager = new EntityManager();
        assert (!entityManager.entityExists(UUID.randomUUID()));
        assert (entityManager.entityExists(entityManager.createEntity()));
    }

    @Test
    public void testGetAllEntities() throws Exception {
        EntityManager entityManager = new EntityManager();
        HashSet<UUID> entities = new HashSet<>();
        for (int i = 0; i < 5; ++i) {
            entities.add(entityManager.createEntity(String.valueOf(i)));
        }
        assert (entityManager.getAllEntities().containsAll(entities));
        assert (entityManager.getAllEntities().equals(entities));
    }

    @Test
    public void testAddComponent() throws Exception {
        EntityManager entityManager = new EntityManager();
        UUID entity = entityManager.createEntity("test");
        TestComponent testComponent = new TestComponent();
        assert (entityManager.addComponent(entity, testComponent));
        assert (!entityManager.getEntityComponentMapDirectly().isEmpty());
        assert (entityManager.getEntityComponentMapDirectly().get(entity).get(TestComponent.class).equals(testComponent));
        assert (entityManager.getEntityComponentMapDirectly().get(entity).get(TestComponent.class) == testComponent);
        assert (!entityManager.getComponentMapByClassDirectly().isEmpty());
        assert (entityManager.getComponentMapByClassDirectly().get(TestComponent.class).get(entity).equals(testComponent));
        assert (entityManager.getComponentMapByClassDirectly().get(TestComponent.class).get(entity) == testComponent);
        assert (!entityManager.addComponent(entity, testComponent));
    }

    @Test
    public void testRemoveComponent() throws Exception {
        EntityManager entityManager = new EntityManager();
        UUID entity = entityManager.createEntity("test");
        assert (!entityManager.removeComponent(entity, TestComponent.class));
        TestComponent testComponent = new TestComponent();
        TestComponent2 testComponent2 = new TestComponent2();
        TestComponent3 testComponent3 = new TestComponent3();
        entityManager.addComponent(entity, testComponent);
        assert (entityManager.removeComponent(entity, testComponent.getClass()));
        assert (entityManager.getEntityComponentMapDirectly().isEmpty());
        assert (entityManager.getComponentMapByClassDirectly().isEmpty());
        entityManager.addComponent(entity, testComponent);
        entityManager.addComponent(entity, testComponent2);
        entityManager.addComponent(entity, testComponent3);
        assert (entityManager.removeComponent(entity, testComponent2.getClass()));
        assert (!entityManager.getEntityComponentMapDirectly().isEmpty());
        assert (entityManager.getEntityComponentMapDirectly().get(entity).containsKey(TestComponent.class));
        assert (entityManager.getEntityComponentMapDirectly().get(entity).get(TestComponent.class).equals(testComponent));
        assert (entityManager.getEntityComponentMapDirectly().get(entity).get(TestComponent.class) == testComponent);
        assert (!entityManager.getEntityComponentMapDirectly().get(entity).containsKey(TestComponent2.class));
        assert (!entityManager.getEntityComponentMapDirectly().get(entity).containsValue(testComponent2));
        assert (entityManager.getEntityComponentMapDirectly().get(entity).get(TestComponent2.class) == null);
        assert (entityManager.getEntityComponentMapDirectly().get(entity).containsKey(TestComponent3.class));
        assert (entityManager.getEntityComponentMapDirectly().get(entity).get(TestComponent3.class).equals(testComponent3));
        assert (entityManager.getEntityComponentMapDirectly().get(entity).get(TestComponent3.class) == testComponent3);
    }

    @Test
    public void testGetAllEntitiesPossessingComponent() throws Exception {
        EntityManager entityManager = new EntityManager();
        assert (entityManager.getAllEntitiesPossessingComponent(TestComponent.class).isEmpty());
        HashSet<UUID> entitiesContainingComponent2 = new HashSet<>();
        for (int i = 0; i < 5; ++i) {
            entityManager.createEntity(String.valueOf(i));
        }
        for (int i = 5; i < 10; ++i) {
            UUID entity = entityManager.createEntity(String.valueOf(i));
            entityManager.addComponent(entity, new TestComponent());
        }
        for (int i = 10; i < 15; ++i) {
            UUID entity = entityManager.createEntity(String.valueOf(i));
            entityManager.addComponent(entity, new TestComponent2());
            entitiesContainingComponent2.add(entity);
        }
        for (int i = 15; i < 20; ++i) {
            UUID entity = entityManager.createEntity(String.valueOf(i));
            entityManager.addComponent(entity, new TestComponent());
            entityManager.addComponent(entity, new TestComponent2());
            entityManager.addComponent(entity, new TestComponent3());
            entitiesContainingComponent2.add(entity);
        }
        for (int i = 20; i < 25; ++i) {
            UUID entity = entityManager.createEntity(String.valueOf(i));
            entityManager.addComponent(entity, new TestComponent());
            entityManager.addComponent(entity, new TestComponent3());
        }
        assert (entityManager.getAllEntitiesPossessingComponent(TestComponent2.class).containsAll(entitiesContainingComponent2));
        assert (entityManager.getAllEntitiesPossessingComponent(TestComponent2.class).equals(entitiesContainingComponent2));
    }

    @Test
    public void testGetComponentTypesForEntity() throws Exception {
        EntityManager entityManager = new EntityManager();
        UUID entity = entityManager.createEntity("entity");
        assert (entityManager.getComponentTypesForEntity(entity).isEmpty());
        HashSet<Class<? extends Component>> componentClasses = new HashSet<>();
        componentClasses.add(TestComponent.class);
        entityManager.addComponent(entity, new TestComponent());
        assert (entityManager.getComponentTypesForEntity(entity).containsAll(componentClasses));
        assert (entityManager.getComponentTypesForEntity(entity).equals(componentClasses));
        componentClasses.add(TestComponent2.class);
        entityManager.addComponent(entity, new TestComponent2());
        assert (entityManager.getComponentTypesForEntity(entity).containsAll(componentClasses));
        assert (entityManager.getComponentTypesForEntity(entity).equals(componentClasses));
        componentClasses.add(TestComponent3.class);
        entityManager.addComponent(entity, new TestComponent3());
        assert (entityManager.getComponentTypesForEntity(entity).containsAll(componentClasses));
        assert (entityManager.getComponentTypesForEntity(entity).equals(componentClasses));
    }

    @Test
    public void testGetEntityComponentMapByClass() throws Exception {
        EntityManager entityManager = new EntityManager();
        assert (entityManager.getEntityComponentMapByClass(TestComponent.class).isEmpty());
        HashSet<UUID> entitiesWithTestComponent = new HashSet<>();
        entityManager.createEntity();
        entityManager.createEntity();
        UUID entity = entityManager.createEntity();
        entityManager.addComponent(entity, new TestComponent());
        entitiesWithTestComponent.add(entity);
        entity = entityManager.createEntity();
        entityManager.addComponent(entity, new TestComponent());
        entitiesWithTestComponent.add(entity);
        entity = entityManager.createEntity();
        entityManager.addComponent(entity, new TestComponent2());
        entity = entityManager.createEntity();
        entityManager.addComponent(entity, new TestComponent3());
        entity = entityManager.createEntity();
        entityManager.addComponent(entity, new TestComponent());
        entitiesWithTestComponent.add(entity);
        assert (entityManager.getEntityComponentMapByClass(TestComponent.class).keySet().containsAll(entitiesWithTestComponent));
        assert (entityManager.getEntityComponentMapByClass(TestComponent.class).keySet().equals(entitiesWithTestComponent));
    }

    @Test
    public void testGetComponent() throws Exception {
        EntityManager entityManager = new EntityManager();
        UUID entity = entityManager.createEntity("entity");
        TestComponent testComponent = new TestComponent();
        assert (entityManager.getComponent(entity, TestComponent.class) == null);
        entityManager.addComponent(entity, testComponent);
        assert (entityManager.getComponent(entity, TestComponent.class) == testComponent);
    }

    @Test
    public void testHasComponent() throws Exception {
        EntityManager entityManager = new EntityManager();
        UUID entity = entityManager.createEntity("entity");
        assert (!entityManager.hasComponent(entity, TestComponent.class));
        entityManager.addComponent(entity, new TestComponent());
        assert (entityManager.hasComponent(entity, TestComponent.class));
        entityManager.removeComponent(entity, TestComponent.class);
        assert (!entityManager.hasComponent(entity, TestComponent.class));
        entityManager.addComponent(entity, new TestComponent());
        entityManager.addComponent(entity, new TestComponent2());
        entityManager.addComponent(entity, new TestComponent3());
        assert (entityManager.hasComponent(entity, TestComponent.class));
        entityManager.removeComponent(entity, TestComponent.class);
        assert (!entityManager.hasComponent(entity, TestComponent.class));
    }

    @Test
    public void testAssertEntityExists() throws Exception {
        try {
            new EntityManager().assertEntityExists(UUID.randomUUID(), "testAssertEntityExists");
            org.junit.Assert.fail("Expected NonExistentEntityException");
        } catch (NonExistentEntityException expected) {/* test passed */ }
    }

    @Test
    public void testSetEntityName() throws Exception {
        EntityManager entityManager = new EntityManager();
        UUID entity = entityManager.createEntity();
        UUID namedEntity = entityManager.createEntity("name");
        assert (entityManager.getEntityName(entity) == null);
        assert (entityManager.getEntityName(namedEntity).equals("name"));
        entityManager.setEntityName(entity, "some name");
        assert (entityManager.getEntityName(entity).equals("some name"));
        entityManager.setEntityName(namedEntity, "new name");
        assert (entityManager.getEntityName(namedEntity).equals("new name"));
        entityManager.setEntityName(entity, "new name");
        assert (entityManager.getEntityName(entity).equals("new name"));
    }

    @Test
    public void testGetEntityName() throws Exception {
        EntityManager entityManager = new EntityManager();
        UUID entity = entityManager.createEntity("name");
        assert (entityManager.getEntityNameMapDirectly().containsKey(entity));
        assert (entityManager.getEntityNameMapDirectly().get(entity).equals("name"));
        assert (entityManager.getEntityName(entity).equals("name"));
    }

    @Test
    public void testRemoveEntityName() throws Exception {
        EntityManager entityManager = new EntityManager();
        UUID entity = entityManager.createEntity("name");
        assert (entityManager.getEntityName(entity).equals("name"));
        entityManager.removeEntityName(entity);
        assert (!entityManager.getEntityNameMapDirectly().containsKey(entity));
        assert (!entityManager.getEntityNameMapDirectly().containsValue("name"));
        assert (entityManager.getEntityName(entity) == null);
    }
}