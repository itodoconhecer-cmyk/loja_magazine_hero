package aulas.umc.oo; import aulas.umc.oo.pipeline.ProcessContext; import org.junit.jupiter.api.Test; import static org.junit.jupiter.api.Assertions.*;
class PipelineContextTest { @Test void deveGuardarERecuperarDados(){var c=new ProcessContext().put("x",10);assertEquals(10,c.get("x",Integer.class));} }
