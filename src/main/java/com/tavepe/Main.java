package com.tavepe;

import org.lwjgl.*;
import org.lwjgl.opengl.*;

import java.nio.*;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Main {

    final int WIDTH = 100, HEIGHT = 100, SIZE = 6;
    Pixel[][] world = new Pixel[WIDTH][HEIGHT];

    private long window;

    public void run() {
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                world[i][j] = new Air(); // Properly initialize the world array with Air objects
            }
        }
        init();
        loop();

        glfwDestroyWindow(window);
        glfwTerminate();
    }

    private void init() {
        if (!glfwInit()) {
            throw new IllegalStateException("Não foi possível inicializar o GLFW");
        }

        window = glfwCreateWindow(WIDTH * SIZE, HEIGHT * SIZE, "Simulação de Areia", NULL, NULL);
        if (window == NULL) {
            throw new RuntimeException("Falha ao criar janela GLFW");
        }

        glfwMakeContextCurrent(window);
        glfwSwapInterval(1); // V-Sync

        GL.createCapabilities();
    }

    private void loop() {
        while (!glfwWindowShouldClose(window)) {
            glClear(GL_COLOR_BUFFER_BIT);

            refreshWorld();
            render();

            glfwSwapBuffers(window);
            glfwPollEvents();

            if (glfwGetMouseButton(window, GLFW_MOUSE_BUTTON_LEFT) == GLFW_PRESS) {
                DoubleBuffer xpos = BufferUtils.createDoubleBuffer(1);
                DoubleBuffer ypos = BufferUtils.createDoubleBuffer(1);
                glfwGetCursorPos(window, xpos, ypos);
                int x = (int) (xpos.get(0) / SIZE);
                int y = (int) (ypos.get(0) / SIZE);


                if (x >= 2 && x < WIDTH - 2 && y >= 0 && y < HEIGHT) {
                    world[x][y] = new Sand();
                }
            }
        }
    }

    private void render() {
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {

                glColor3f(world[x][y].getColor()[0], world[x][y].getColor()[1], world[x][y].getColor()[2]); // Amarelo


                float xf = x * SIZE;
                float yf = (HEIGHT - 1 - y) * SIZE;


                float winWidth = WIDTH * SIZE;
                float winHeight = HEIGHT * SIZE;

                // Normalizar para OpenGL (-1 a 1)
                float x0 = (2f * xf) / winWidth - 1f;
                float y0 = (2f * yf) / winHeight - 1f;
                float x1 = (2f * (xf + SIZE)) / winWidth - 1f;
                float y1 = (2f * (yf + SIZE)) / winHeight - 1f;

                glBegin(GL_QUADS);
                glVertex2f(x0, y0);
                glVertex2f(x1, y0);
                glVertex2f(x1, y1);
                glVertex2f(x0, y1);
                glEnd();
            }
        }
    }

    private void refreshWorld() {
        for (int y = HEIGHT - 3; y >= 0; y--) {
            for (int x = 2; x < WIDTH - 2; x++) {
                if (world[x][y].getId() == 1 && world[x][y + 1].getId() == 0) {
                    world[x][y + 1] = new Sand();
                    world[x][y] = new Air();
                } else if (world[x][y].getId() == 1 && world[x][y + 2].getId() == 1) {
                    if (world[x + 1][y + 1].getId() == 0) {
                        world[x][y] = new Air();
                        world[x + 1][y] = new Sand();
                    }
                    if (world[x - 1][y + 1].getId() == 0) {
                        world[x][y] = new Air();
                        world[x - 1][y] = new Sand();
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        new Main().run();
    }
}
