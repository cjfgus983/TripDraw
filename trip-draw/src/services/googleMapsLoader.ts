// src/services/googleMapsLoader.ts
import { Loader } from "@googlemaps/js-api-loader";

let loader: Loader | null = null;

export function getGoogleMapsLoader(): Loader {
  if (!loader) {
    loader = new Loader({
      apiKey: import.meta.env.VITE_GOOGLE_MAPS_API_KEY,
      version: "weekly",
      libraries: ['places'], // 필요하다면 ["places","geometry"] 등 추가
    });
  }
  return loader;
}
