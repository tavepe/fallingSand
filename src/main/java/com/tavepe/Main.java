package com.tavepe;

import org.lwjgl.*;
import org.lwjgl.opengl.*;

import java.nio.*;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Main {

    final int WIDTH = 100, HEIGHT = 100, SIZE = 6;
    int[][] world = new int[WIDTH][HEIGHT];

    private long window;

    public void run() {
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
                    world[x][y] = 1;
                }
            }
        }
    }

    private void render() {
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                if (world[x][y] == 1) {
                    glColor3f(1f, 1f, 0f); // Amarelo
                } else {
                    glColor3f(0f, 0f, 0f); // Preto
                }

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
                if (world[x][y] == 1 && world[x][y + 1] == 0) {
                    world[x][y + 1] = 1;
                    world[x][y] = 0;
                } else if (world[x][y] == 1 && world[x][y + 2] == 1) {
                    if (world[x + 1][y + 1] == 0) {
                        world[x][y] = 0;
                        world[x + 1][y] = 1;
                    }
                    if (world[x - 1][y + 1] == 0) {
                        world[x][y] = 0;
                        world[x - 1][y] = 1;
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        new Main().run();
    }
}
