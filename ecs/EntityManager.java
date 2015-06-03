package ecs;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class EntityManager {
    private final HashSet<UUID> allEntities = new HashSet<>();
    private final HashMap<UUID, String> entityNameMap = new HashMap<>();
    private final HashMap<Class<? extends Component>, HashMap<UUID, Component>> componentMapByClass = new HashMap<>();
    private final HashMap<UUID, HashMap<Class<? extends Component>, Component>> entityComponentMap = new HashMap<>();

    public synchronized UUID createEntity() {
        UUID entity;
        do {
            entity = UUID.randomUUID();
        } while (!allEntities.add(entity));
        return entity;
    }

    public synchronized UUID createEntity(String name) {
        UUID entity = createEntity();
        entityNameMap.put(entity, name);
        return entity;
    }

    public synchronized boolean addEntity(UUID entity) {
        return allEntities.add(entity);
    }

    public synchronized boolean addEntity(UUID entity, String name) {
        if (addEntity(entity)) {
            entityNameMap.put(entity, name);
            return true;
        }
        return false;
    }

    public synchronized void killEntity(UUID entity) throws NonExistentEntityException {
        assertEntityExists(entity, "killEntity");
        removeEntityName(entity);
        if (entityComponentMap.containsKey(entity)) {
            HashMap<Class<? extends Component>, Component> removedComponentMap = entityComponentMap.remove(entity);
            for (Map.Entry<Class<? extends Component>, Component> entry : removedComponentMap.entrySet()) {
                componentMapByClass.get(entry.getKey()).remove(entity, entry.getValue());
                if (componentMapByClass.get(entry.getKey()).isEmpty()) {
                    componentMapByClass.remove(entry.getKey());
                }
            }
        }
        allEntities.remove(entity);
    }

    public synchronized boolean entityExists(UUID entity) {
        return allEntities.contains(entity);
    }

    public synchronized HashSet<UUID> getAllEntities() {
        return new HashSet<>(allEntities);
    }

    public synchronized <T extends Component> boolean addComponent(UUID entity, T component) throws NonExistentEntityException {
        assertEntityExists(entity, "addComponent");
        if (!entityComponentMap.containsKey(entity)) {
            entityComponentMap.put(entity, new HashMap<>());
        }
        HashMap<Class<? extends Component>, Component> componentTypeMap = entityComponentMap.get(entity);
        if (componentTypeMap.containsKey(component.getClass())) {
            return false;
        }
        HashMap<UUID, Component> entityComponentMap = componentMapByClass.get(component.getClass());
        if (entityComponentMap == null) {
            entityComponentMap = new HashMap<>();
            componentMapByClass.put(component.getClass(), entityComponentMap);
        }
        entityComponentMap.put(entity, component);
        componentTypeMap.put(component.getClass(), component);
        return true;
    }

    public synchronized <T extends Component> boolean removeComponent(UUID entity, Class<T> componentClass) throws NonExistentEntityException {
        assertEntityExists(entity, "removeComponent");
        HashMap<Class<? extends Component>, Component> componentTypes = entityComponentMap.get(entity);
        HashMap<UUID, Component> entityComponents = componentMapByClass.get(componentClass);
        if (componentTypes == null || !componentTypes.containsKey(componentClass)) {
            return false;
        }
        componentTypes.remove(componentClass);
        if (componentTypes.isEmpty()) {
            entityComponentMap.remove(entity);
        }
        entityComponents.remove(entity);
        if (entityComponents.isEmpty()) {
            componentMapByClass.remove(componentClass);
        }
        return true;
    }

    public synchronized <T extends Component> Set<UUID> getAllEntitiesPossessingComponent(Class<T> componentClass) {
        if (!componentMapByClass.containsKey(componentClass)) {
            return new HashSet<>();
        }
        return componentMapByClass.get(componentClass).keySet();
    }

    public synchronized Set<Class<? extends Component>> getComponentTypesForEntity(UUID entity) throws NonExistentEntityException {
        assertEntityExists(entity, "getComponentTypesForEntity");
        if (!entityComponentMap.containsKey(entity)) {
            return new HashSet<>();
        }
        return entityComponentMap.get(entity).keySet();
    }

    @SuppressWarnings("unchecked")
    public synchronized <T extends Component> HashMap<UUID, T> getEntityComponentMapByClass(Class<T> componentClass) {
        if (!componentMapByClass.containsKey(componentClass)) {
            return new HashMap<>();
        }
        return (HashMap<UUID, T>) componentMapByClass.get(componentClass);
    }

    public synchronized <T extends Component> T getComponent(UUID entity, Class<T> componentClass) throws NonExistentEntityException {
        assertEntityExists(entity, "getComponent");
        if (!entityComponentMap.containsKey(entity)) {
            return null;
        }
        return componentClass.cast(entityComponentMap.get(entity).get(componentClass));
    }

    public synchronized <T extends Component> boolean hasComponent(UUID entity, Class<T> componentClass) throws NonExistentEntityException {
        assertEntityExists(entity, "hasComponent");
        return getComponentTypesForEntity(entity).contains(componentClass);
    }

    public synchronized void setEntityName(UUID entity, String name) throws NonExistentEntityException {
        assertEntityExists(entity, "setEntityName");
        entityNameMap.put(entity, name);
    }

    public synchronized String getEntityName(UUID entity) throws NonExistentEntityException {
        assertEntityExists(entity, "getEntityName");
        return entityNameMap.get(entity);
    }

    public synchronized void removeEntityName(UUID entity) throws NonExistentEntityException {
        assertEntityExists(entity, "removeEntityName");
        entityNameMap.remove(entity);
    }

    protected synchronized void assertEntityExists(UUID entity, String method) throws NonExistentEntityException {
        if (!entityExists(entity)) {
            throw new NonExistentEntityException(method + " failed: Entity " + entity + " does not exist!");
        }
    }

    //expose the internal variables to the test
    protected HashSet<UUID> getAllEntitiesSetDirectly() {
        return allEntities;
    }

    protected HashMap<UUID, String> getEntityNameMapDirectly() {
        return entityNameMap;
    }

    protected HashMap<Class<? extends Component>, HashMap<UUID, Component>> getComponentMapByClassDirectly() {
        return componentMapByClass;
    }

    protected HashMap<UUID, HashMap<Class<? extends Component>, Component>> getEntityComponentMapDirectly() {
        return entityComponentMap;
    }
}
