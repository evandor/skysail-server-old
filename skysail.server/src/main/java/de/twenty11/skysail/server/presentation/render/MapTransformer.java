//package de.twenty11.skysail.server.presentation.render;
//
//import java.util.Map;
//
//public class MapTransformer {
//
//    private Map<String, Object> map;
//
//    public MapTransformer(Map<String, Object> input) {
//        this.map = input;
//    }
//
//    public MapTransformer clean(CleaningStrategy cleaningStrategy) {
//        this.map = cleaningStrategy.clean(map);
//        return this;
//    }
//
//    public RendererInput asRendererInput() {
//        return new RendererInput(this.map);
//    }
//
// }
