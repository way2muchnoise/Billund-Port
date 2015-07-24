package billund.reference;

public class Textures
{
    public static final String RESOURCE_PREFIX = Reference.ID.toLowerCase() + ":";
    public static final String COLOURS_PREFIX = "colours/";
    public static final String TEXTURES = "textures/";

    public static final class GUI
    {
        private static final String GUI_PREFIX = TEXTURES + "gui/";
        public static final String ORDER_FORM = GUI_PREFIX + "OrderForm.png";
    }

    public static final class Model
    {
        private static final String MODEL_PREFIX = TEXTURES + "models/";
        public static final String PARACHUTE = MODEL_PREFIX + "chute.png";
    }
}
