<template>
    <div class="home-page animate-fade-in">
        <div class="home-bg" :style="bgStyle"></div>

        <div class="slider-container" @mousemove="handleMouseMove" @mouseleave="resetTilt" @mousedown="startDragging"
            @mouseup="stopDragging" @touchstart="startDragging" @touchend="stopDragging" @touchmove="handleTouchMove">

            <!-- Side Arrows (Fixed relative to container) -->
            <button @click.stop="prev" class="side-arrow-premium left" aria-label="Anterior">
                <i class="bi bi-chevron-left"></i>
            </button>
            <button @click.stop="next" class="side-arrow-premium right" aria-label="Siguiente">
                <i class="bi bi-chevron-right"></i>
            </button>

            <div class="slider-wrapper" :style="wrapperStyle">
                <div v-for="(slide, index) in slides" :key="index" class="slide-card" :class="getSlideClass(index)"
                    :style="{ zIndex: index === currentIdx ? 100 : 20 }">

                    <div class="card-inner" :style="getCardStyle(index)" @click="handleSlideClick(index)">
                        <div class="card-image" :style="{ backgroundImage: `url(${slide.image})` }"></div>
                        <div class="card-overlay"></div>
                        <div class="card-content">
                            <span class="card-category">{{ slide.category }}</span>
                            <h2 class="card-title">{{ slide.title }}</h2>
                            <p class="card-description">{{ slide.description }}</p>
                            <router-link :to="slide.link" class="btn-card" @click.stop>
                                Explorar <i class="bi bi-arrow-right-short"></i>
                            </router-link>
                        </div>
                    </div>
                </div>
            </div>

        </div>
    </div>
</template>

<script>
export default {
    name: 'HomeView',
    data() {
        return {
            currentIdx: 0,
            mouse: { x: 0, y: 0 },
            tilt: { x: 0, y: 0 },
            lerpAmount: 0.08,
            slides: [
                {
                    title: 'Gestión de Pacientes',
                    category: 'Administración',
                    description: 'Acceda a expedientes, historiales y registros de pacientes en tiempo real.',
                    image: 'https://images.unsplash.com/photo-1519494026892-80bbd2d6fd0d?auto=format&fit=crop&q=80&w=1000',
                    link: '/paciente',
                    color: '#6366f1'
                },
                {
                    title: 'Control de Agenda',
                    category: 'Planificación',
                    description: 'Organice citas médicas y optimice el flujo de trabajo del staff.',
                    image: 'https://images.unsplash.com/photo-1516549655169-df83a0774514?q=80&w=1000&t=' + Date.now(),
                    link: '/citas',
                    color: '#14b8a6'
                },
                {
                    title: 'Staff de Especialistas',
                    category: 'Médico',
                    description: 'Gestione el equipo de doctores y sus disponibilidades consultorias.',
                    image: 'https://images.unsplash.com/photo-1581056771107-24ca5f033842?auto=format&fit=crop&q=80&w=1000',
                    link: '/doctor',
                    color: '#f43f5e'
                }
            ],
            rafId: null,
            isDragging: false,
            startX: 0
        };
    },
    computed: {
        bgStyle() {
            const activeSlide = this.slides[this.currentIdx];
            return {
                backgroundImage: `url(${activeSlide.image})`,
                '--accent-color': activeSlide.color
            };
        },
        wrapperStyle() {
            return {
                transform: `rotateX(${this.tilt.y}deg) rotateY(${this.tilt.x}deg)`
            };
        }
    },
    mounted() {
        this.startAnimation();
    },
    beforeUnmount() {
        cancelAnimationFrame(this.rafId);
    },
    methods: {
        startAnimation() {
            const loop = () => {
                // Lerp for smooth tilt
                const targetTiltX = (this.mouse.x * 35); // Target degrees
                const targetTiltY = (this.mouse.y * -20); // Reduced vertical tilt limit

                this.tilt.x += (targetTiltX - this.tilt.x) * this.lerpAmount;
                this.tilt.y += (targetTiltY - this.tilt.y) * this.lerpAmount;

                this.rafId = requestAnimationFrame(loop);
            };
            loop();
        },
        handleMouseMove(e) {
            const rect = e.currentTarget.getBoundingClientRect();
            const x = (e.clientX - rect.left) / rect.width - 0.5;
            const y = (e.clientY - rect.top) / rect.height - 0.5;
            this.mouse.x = x;
            this.mouse.y = y;
        },
        resetTilt() {
            this.mouse.x = 0;
            this.mouse.y = 0;
        },
        getSlideClass(i) {
            if (i === this.currentIdx) return 'active';
            if (i === (this.currentIdx - 1 + this.slides.length) % this.slides.length) return 'prev';
            if (i === (this.currentIdx + 1) % this.slides.length) return 'next';
            return 'hidden';
        },
        getCardStyle(i) {
            const diff = i - this.currentIdx;
            // Additional parallax effects per layer if needed
            return {};
        },
        next() {
            this.currentIdx = (this.currentIdx + 1) % this.slides.length;
        },
        prev() {
            this.currentIdx = (this.currentIdx - 1 + this.slides.length) % this.slides.length;
        },
        goTo(i) {
            this.currentIdx = i;
        },
        handleSlideClick(i) {
            if (i !== this.currentIdx) {
                this.goTo(i);
            }
        },
        startDragging(e) {
            this.isDragging = true;
            this.startX = e.clientX || e.touches[0].clientX;
        },
        stopDragging(e) {
            if (!this.isDragging) return;
            const endX = e.clientX || e.changedTouches[0].clientX;
            const diff = endX - this.startX;
            if (Math.abs(diff) > 50) {
                if (diff > 0) this.prev();
                else this.next();
            }
            this.isDragging = false;
        },
        handleTouchMove(e) {
            if (!this.isDragging) return;
            // Prevent scrolling while dragging slider
            e.preventDefault();
        }
    }
};
</script>

<style scoped>
.home-page {
    height: calc(100vh - 70px);
    width: 100vw;
    position: relative;
    overflow: hidden;
    display: flex;
    align-items: center;
    justify-content: center;
    perspective: 2500px;
}

.home-bg {
    position: absolute;
    top: -10%;
    left: -10%;
    width: 120%;
    height: 120%;
    background-size: cover;
    background-position: center;
    filter: blur(60px) brightness(0.3);
    z-index: -1;
    transition: background-image 1.2s cubic-bezier(0.4, 0, 0.2, 1);
}

.home-bg::after {
    content: '';
    position: absolute;
    inset: 0;
    background: radial-gradient(circle at center, transparent, rgba(15, 23, 42, 0.9));
}

.slider-container {
    width: 100%;
    height: 100%;
    display: flex;
    align-items: center;
    justify-content: center;
    position: relative;
    transform-style: preserve-3d;
}

.slider-wrapper {
    transform-style: preserve-3d;
    width: 100%;
    height: 100%;
    display: flex;
    align-items: center;
    justify-content: center;
    position: relative;
    transition: transform 0.1s ease-out;
}

.slide-card {
    position: absolute;
    width: min(420px, 85vw);
    height: min(600px, 60vh);
    transform-style: preserve-3d;
    transition: all 0.9s cubic-bezier(0.23, 1, 0.32, 1);
    cursor: pointer;
    user-select: none;
}

.slide-card.active {
    transform: translate3d(0, 0, 250px) rotateY(0deg);
    z-index: 100 !important;
    opacity: 1;
    pointer-events: auto;
}

.slide-card.prev {
    transform: translate3d(-460px, 0, -200px) rotateY(40deg);
    z-index: 50 !important;
    opacity: 0.5;
    filter: grayscale(0.4) blur(1px);
    pointer-events: auto;
}

.slide-card.next {
    transform: translate3d(460px, 0, -200px) rotateY(-40deg);
    z-index: 50 !important;
    opacity: 0.5;
    filter: grayscale(0.4) blur(1px);
    pointer-events: auto;
}

.slide-card.hidden {
    transform: translate3d(0, 0, -1000px) scale(0.5);
    opacity: 0;
    z-index: 10;
}

.card-inner {
    width: 100%;
    height: 100%;
    background: var(--card-surface);
    border-radius: 40px;
    overflow: hidden;
    box-shadow: 0 50px 100px rgba(0, 0, 0, 0.7);
    position: relative;
    border: 1px solid rgba(255, 255, 255, 0.1);
    transform-style: preserve-3d;
}

.card-image {
    position: absolute;
    inset: 0;
    background-size: cover;
    background-position: center;
    transition: transform 1.2s ease;
}

.slide-card.active:hover .card-image {
    transform: scale(1.1) translateZ(30px);
}

.card-overlay {
    position: absolute;
    inset: 0;
    background: linear-gradient(to top,
            rgba(15, 23, 42, 0.95) 0%,
            rgba(15, 23, 42, 0.2) 50%,
            transparent 100%);
}

.card-content {
    position: absolute;
    bottom: 0;
    left: 0;
    padding: 3rem;
    color: white;
    transform: translateZ(100px);
    width: 100%;
}

.card-category {
    display: inline-block;
    padding: 0.4rem 1rem;
    background: rgba(255, 255, 255, 0.15);
    backdrop-filter: blur(10px);
    border-radius: 99px;
    font-size: 0.75rem;
    font-weight: 700;
    text-transform: uppercase;
    letter-spacing: 2px;
    margin-bottom: 1rem;
    border: 1px solid rgba(255, 255, 255, 0.1);
}

.card-title {
    font-size: 2.2rem;
    font-weight: 800;
    margin-bottom: 1rem;
    line-height: 1.1;
    text-shadow: 0 10px 20px rgba(0, 0, 0, 0.3);
}

.card-description {
    font-size: 0.95rem;
    opacity: 0.8;
    margin-bottom: 2rem;
    display: -webkit-box;
    -webkit-line-clamp: 2;
    line-clamp: 2;
    -webkit-box-orient: vertical;
    overflow: hidden;
}

.btn-card {
    display: inline-flex;
    align-items: center;
    padding: 0.8rem 1.8rem;
    background: white;
    color: #0f172a;
    border-radius: 12px;
    text-decoration: none;
    font-weight: 700;
    font-size: 0.9rem;
    transition: all 0.3s;
}

.btn-card:hover {
    transform: scale(1.05);
    background: var(--primary);
    color: white;
}

.dot.active {
    width: 30px;
    border-radius: 5px;
    background: white;
}

@media (max-width: 768px) {
    .slide-card.prev {
        transform: translate3d(-150px, 0, -300px) rotateY(20deg);
        opacity: 0.2;
    }

    .slide-card.next {
        transform: translate3d(150px, 0, -300px) rotateY(-20deg);
        opacity: 0.2;
    }

    .card-title {
        font-size: 1.8rem;
    }
}

/* Side Navigation Arrows */
.side-arrow-premium {
    position: absolute;
    top: 50%;
    transform: translateY(-50%);
    width: 60px;
    height: 60px;
    background: rgba(255, 255, 255, 0.05);
    backdrop-filter: blur(15px);
    border: 1px solid rgba(255, 255, 255, 0.1);
    color: white;
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 1.5rem;
    cursor: pointer;
    z-index: 1000;
    transition: all 0.4s cubic-bezier(0.175, 0.885, 0.32, 1.275);
    box-shadow: 0 10px 30px rgba(0, 0, 0, 0.2);
}

.side-arrow-premium.left {
    left: 40px;
}

.side-arrow-premium.right {
    right: 40px;
}

.side-arrow-premium:hover {
    background: var(--primary);
    transform: translateY(-50%) scale(1.1);
    box-shadow: 0 15px 40px rgba(99, 102, 241, 0.4);
    border-color: rgba(255, 255, 255, 0.3);
}

.side-arrow-premium:active {
    transform: translateY(-50%) scale(0.95);
}

@media (max-width: 1200px) {
    .side-arrow-premium {
        width: 50px;
        height: 50px;
        font-size: 1.2rem;
    }

    .side-arrow-premium.left {
        left: 15px;
    }

    .side-arrow-premium.right {
        right: 15px;
    }
}

@media (max-width: 768px) {
    .side-arrow-premium {
        display: none;
    }

    /* Use swipe on mobile */
}
</style>
