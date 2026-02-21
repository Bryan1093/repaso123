import { reactive } from 'vue';

export const toastState = reactive({
    show: false,
    message: '',
    type: 'success', // 'success' | 'error' | 'info'
    id: 0
});

export const notify = (msg, type = 'success') => {
    toastState.message = msg;
    toastState.type = type;
    toastState.show = true;
    toastState.id++;

    const currentId = toastState.id;
    setTimeout(() => {
        if (toastState.id === currentId) {
            toastState.show = false;
        }
    }, 4000);
};

export default notify;
